/******************************************************************************
 * This piece of work is to enhance errorhospitalsystem project functionality.*
 *                                                                            *
 * Author:    eomisore                                                        *
 * File:      ErrorHospitalRESTService.java                                   *
 * Created:   19/10/2024, 01:29                                               *
 * Modified:  19/10/2024, 01:29                                               *
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

package com.aerosimo.ominet.api;

import com.aerosimo.ominet.dto.ErrorInput;
import com.aerosimo.ominet.dto.ErrorResponse;
import com.aerosimo.ominet.dto.ErrorResult;
import com.aerosimo.ominet.dao.ErrorHospital;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Path("/errorhospital")
public class ErrorHospitalRESTService {

    private static final Logger log;

    static {
        log = LogManager.getLogger(ErrorHospitalRESTService.class.getName());
    }
    static String Response;

    @POST
    @Path("/record")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ErrorResponse recordError(ErrorInput input){
        if (input.getFaultcode() == null || input.getFaultcode().isEmpty()) {
            Response = "Schema Validation failed because Fault Code is a required field";
            log.error(Response);
        } else if (input.getFaultmessage() == null || input.getFaultmessage().isEmpty()) {
            Response = "Schema Validation failed because Fault Message is a required field";
            log.error(Response);
        } else if (input.getFaultservicename() == null || input.getFaultservicename().isEmpty()) {
            Response = "Schema Validation failed because Service Name is a required field";
            log.error(Response);
        }
        Response = ErrorHospital.recordError(input.getFaultcode(), input.getFaultmessage(), input.getFaultservicename());
        log.info(Response);
        return new ErrorResponse(Response);
    }

    @GET
    @Path("/top/{records}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ErrorResult> getTopErrors(@PathParam("records") int records) {
        return ErrorHospital.getTopErrors(records);
    }
}