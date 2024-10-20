/******************************************************************************
 * This piece of work is to enhance errorhospitalsystem project functionality.*
 *                                                                            *
 * Author:    eomisore                                                        *
 * File:      ErrorHospital.java                                              *
 * Created:   18/10/2024, 19:07                                               *
 * Modified:  18/10/2024, 19:07                                               *
 *                                                                            *
 * Copyright (c)  2024.  Aerosimo Ltd                                         *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,            *
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES            *
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND                   *
 * NONINFINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT                 *
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,               *
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING               *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE                 *
 * OR OTHER DEALINGS IN THE SOFTWARE.                                         *
 *                                                                            *
 ******************************************************************************/

package com.aerosimo.ominet.core;

import com.aerosimo.ominet.bean.ErrorResult;
import oracle.jdbc.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErrorHospital {

    private static final Logger log;

    static {
        log = LogManager.getLogger(ErrorHospital.class.getName());
    }

    static String response;
    static String sql;
    static Connection con;
    static CallableStatement stmt;
    static List<ErrorResult> errorList;

    public static String recordError(String faultcode, String faultmessage, String faultservicename) {
        log.info("Preparing to register new error into Error Hospital");
        try {
            sql = "{call ErrorHospital_pkg.ErrorCollector(?,?,?,?)}";
            con = Connect.hats();
            stmt = con.prepareCall(sql);
            stmt.setString(1, faultcode);
            stmt.setString(2, faultmessage);
            stmt.setString(3, faultservicename);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.execute();
            response = stmt.getString(4);
            log.info("Successfully register new error into Error Hospital with ID: " + response);
        } catch (SQLException err) {
            response = "ErrorHospital.recordError attempt failed";
            log.error("Unknown error occurred in ErrorHospital.recordError with the following - {}", ErrorHospital.class.getName(), err);
        }
        return response;
    }
    public static List<ErrorResult> getTopErrors(int records){

        errorList = new ArrayList<>();
        sql = "{call ErrorHospital_pkg.GetTopErrors(?,?)}";
        ResultSet rs;
        try {
            Connection con;
            con = Connect.hats();
            stmt = con.prepareCall(sql);
            stmt.setInt(1, records);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            rs = (ResultSet) stmt.getObject(2);
            while (rs.next()) errorList.add(new ErrorResult(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)));
        } catch (Exception err) {
            log.error("Database adaptor error occurred in ErrorHub.GetTopErrors with the following - {}", ErrorHospital.class.getName(), err);
        } finally {
            try {
                assert stmt != null;
                stmt.close();
            } catch (SQLException err) {
                log.error("Database adaptor error occurred in ErrorHub.GetTopErrors with the following - {}", ErrorHospital.class.getName(), err);
            }
        }
        return errorList;
    }
}