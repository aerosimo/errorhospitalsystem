/******************************************************************************
 * This piece of work is to enhance errorhospitalsystem project functionality.*
 *                                                                            *
 * Author:    eomisore                                                        *
 * File:      Postmaster.java                                                 *
 * Created:   03/11/2024, 18:12                                               *
 * Modified:  09/11/2024, 14:34                                               *
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

package com.aerosimo.ominet.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Postmaster {

    private static final Logger log;

    static {
        log = LogManager.getLogger(Postmaster.class.getName());
    }
    static String response;
    static String xmlRequest;
    static String htmlContent;
    static String emailTemplate;
    static URL url;
    static HttpURLConnection connection;


    public static String sendEmail(String faultCode, String faultDetails, String faultRef) {

        try {
            // Define the endpoint URL
            url = new URL("http://ominet.aerosimo.com:8082/postalsystem/ws/postmaster");
            connection = (HttpURLConnection) url.openConnection();

            // Set HTTP properties
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
            connection.setDoOutput(true);

            // Define the HTML content
            htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <meta http-equiv="X-UA-Compatible" content="IE=edge">
                        <style>/* inline styles here */</style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="illustration">
                                <a href="https://www.imagebam.com/view/MEC3A8I" target="_blank">
                                    <img src="https://thumbs4.imagebam.com/3a/3d/f2/MEC3A8I_t.jpg" alt="Error Image" />
                                </a>
                            </div>
                            <div class="hgroup">
                                <span class="name">%s</span><br>
                                <h1>Notification Message from ErrorHub</h1>
                                <p><strong>Error Code:</strong> %s</p>
                                <p><strong>Error Message:</strong> %s</p>
                            </div>
                            <div class="hgroup">
                                <h2>Thank you for your attention</h2>
                            </div>
                            <div class="button-wrap">
                                <button class="explore">Aerosimo Support Team</button>
                            </div>
                            <footer>
                                Aerosimo Ltd © <script>document.write(new Date().getFullYear());</script><br>
                                &#9742; &nbsp;+44 012 3456 789 <br>&#9993;&nbsp;support@aerosimo.com
                            </footer>
                            <span style="font-size:10px;">
                                This is a confidential email and may also be privileged. If you are not the intended recipient, please inform us immediately.
                            </span>
                        </div>
                    </body>
                    </html>
                    """;
            emailTemplate = String.format(htmlContent, faultRef, faultCode, faultDetails);
            // Define the XML request body
            xmlRequest = new StringBuilder().append("""
                    <?xml version="1.0" encoding="UTF-8"?>
                                    <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:mail="https://aerosimo.com/api/ws/postmaster">
                                       <soap:Header/>
                                       <soap:Body>
                                          <mail:sendEmail>
                                             <emailAddress>Aerosimo Support<support@aerosimo.com></emailAddress>
                                             <emailSubject>Error Hospital Fault Notification</emailSubject>
                                             <emailMessage><![CDATA[""")
                    .append(emailTemplate)
                    .append("""
                                             ]]></emailMessage>
                                             <emailFiles/>
                                          </mail:sendEmail>
                                       </soap:Body>
                                    </soap:Envelope>
                    """).toString();

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = xmlRequest.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // Check the response code
            int responseCode = connection.getResponseCode();
            log.info("Http Connection Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.info("Message sent successfully");
                response = "Message sent successfully";
            } else {
                log.error("Failed to send email - {}", Postmaster.class.getName(), responseCode);
                response = "Failed to send email";
            }

        } catch (Exception e) {
            response = "Message not sent successfully";
            log.error("Email Notification Service failed with the following - {}", Postmaster.class.getName(), e);
        }
        return response;
    }
}