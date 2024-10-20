/******************************************************************************
 * This piece of work is to enhance errorhospitalsystem project functionality.*
 *                                                                            *
 * Author:    eomisore                                                        *
 * File:      ErrorHospitalService.java                                       *
 * Created:   18/10/2024, 19:18                                               *
 * Modified:  18/10/2024, 19:18                                               *
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

package com.aerosimo.ominet.service;

import com.aerosimo.ominet.bean.ErrorResult;
import com.aerosimo.ominet.core.ErrorHospital;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@WebService(name = "ErrorHospital", serviceName = "ErrorHospitalService",
        portName = "ErrorHospitalPort", targetNamespace = "https://aerosimo.com/api/ws/errorhospital")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class ErrorHospitalService {
    private static final Logger log;

    static {
        log = LogManager.getLogger(ErrorHospitalService.class.getName());
    }
    static String Response;

    @WebMethod(operationName = "recordError")
    @WebResult(name = "recordError", partName = "recordErrorResponse")
    public String recordError(@XmlElement(required = true) @WebParam(name = "faultcode", partName = "recordErrorRequest") String faultcode,
                              @XmlElement(required = true) @WebParam(name = "faultmessage", partName = "recordErrorRequest") String faultmessage,
                              @XmlElement(required = true) @WebParam(name = "faultservicename", partName = "recordErrorRequest") String faultservicename){

        if (faultcode.isEmpty()) {
            Response = "Schema Validation failed because Fault Code is a required field";
            log.error(Response);
        } else if (faultmessage.isEmpty()) {
            Response = "Schema Validation failed because Fault Message is a required field";
            log.error(Response);
        } else if (faultservicename.isEmpty()) {
            Response = "Schema Validation failed because Service Name is a required field";
            log.error(Response);
        }
        Response = ErrorHospital.recordError(faultcode, faultmessage, faultservicename);
        log.info(Response);
        return Response;
    }

    @WebMethod(operationName = "getTopErrors")
    @WebResult(name = "getTopErrors", partName = "getTopErrorsResponse")
    public List<ErrorResult> getTopErrors(@XmlElement(required = true)@WebParam(name = "records", partName = "getTopErrorsRequest") int records) {
        return ErrorHospital.getTopErrors(records);
    }
}