![MIT License.!](/img/MIT.png "MIT")

##### MIT License Copyright (c) 2024 Aerosimo

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.

	The characters, names, events, articles, templates, or information provided by 
	Aerosimo Ltd are fictional and for reference only. While we strive to keep the 
	information up to date and correct, we make no representations or warranties of 
	any kind, express or implied, about the completeness, accuracy, reliability, 
	suitability, or availability with respect to the information, articles, templates, 
	or related graphics contained in this document or any part of the project. 
	Any reliance you place on such information is therefore strictly at your own risk.

---

# Error Hospital System (EHS)

![Project Cover.!](/img/cover.jpg "Error Hospital System (EHS)")
# Error Hospital
> *Error Hospital System (EHS) is a lightweight service that will be implemented and made available across all platforms to facilitate the logging of errors in at different stages of each service, applications across the domain.*

---
This project is a **Error Hospital System (EHS)** built using **core Java**. Error logging mechanism for tracking system anomalies and exposes **SOAP** and **REST** web services. The project also includes a simple **JSP-based web interface** for reviewing and managing errors.

## Project Structure

This `README.md` gives an overview of the project structure, dependencies, and instructions on how to build and deploy the application.

## Features

1. **Error Hospital**: The core logic that record new error and give top errors based on user selection.
2. **SOAP Web Service**: Exposes a `recordError` and `getTopErrors` method.
3. **REST Web Service**: Exposes a `recordError` and `getTopErrors` method in JSON format.
4. **JSP Web Interface**: A user-friendly web interface.

## Getting Started

![Project Codes & Tasks.!](/img/code.jpg "Project Codes and Task")

---

### Prerequisites

>- **Apache TomEE 10**: Make sure TomEE is installed and running.
>- **Java 23**: Ensure you have Java 23 installed as TomEE 10 targets Jakarta EE 10.
>- **Maven**: The project uses Maven for dependency management with any IDE of choice.

### Dependencies

The required dependencies are defined in `pom.xml`. Below are the key dependencies:

- **Jakarta EE 10 API**: Provides JAX-WS support.
- **JAX-WS**: For SOAP web service implementation.
- **JAX-RS**: For RESTful web service implementation.

### Building and Running the Application

1. **Clone the repository**:

    ```bash
    git clone https://github.com/aerosimo/errorhospitalsystem.git
    cd errorhospitalsystem
    ```

2. **Build the project using Maven**:

    ```bash
    mvn clean install
    ```

3. **Deploy the WAR file**:

   After building the project, deploy the generated `WAR` file from the `target/` directory into the `webapps/` folder of your TomEE installation.

4. **Start TomEE**:

   Start TomEE and access the application:

    - SOAP Service: WSDL at `http://localhost:8080/errorhospitalsystem/ws/errorhospital?wsdl`
    - REST Service: `http://localhost:8080/errorhospitalsystem/rs/errorhospital`
    - Web Interface: `http://localhost:8080/errorhospitalsystem/index.jsp`

## Detailed Explanation of Components

### 1. **Luhn Algorithm** (Core Logic)

Located in `com/aerosimo/ominet/core/ErrorHospital.java`, this class implements the Error Hospital algorithm, a simple java class that record new instances and can fetch the existing records based on user specified parameters

### 2. **SOAP Web Service** (JAX-WS)

The SOAP web service is implemented in `com/aerosimo/ominet/service/ErrorHospitalService.java`. It provides the following methods:
- `recordError(String faultcode, String faultmessage, String faultservicename)`: Records a new error instance.
- `getTopErrors(int records)`: Fetches the top error records based on the number provided.

Example SOAP Request for `recordError`:
```xml
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:err="https://aerosimo.com/api/ws/errorhospital">
   <soap:Header/>
   <soap:Body>
      <err:recordError>
         <faultcode>XXX001</faultcode>
         <faultmessage>User bad auth</faultmessage>
         <faultservicename>Web - Login</faultservicename>
      </err:recordError>
   </soap:Body>
</soap:Envelope>
```

Example SOAP Request for `getTopErrors`:
```xml
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:err="https://aerosimo.com/api/ws/errorhospital">
   <soap:Header/>
   <soap:Body>
      <err:getTopErrors>
         <records>1</records>
      </err:getTopErrors>
   </soap:Body>
</soap:Envelope>
```
### 3. **REST Web Service** (JAX-RS)

The REST web service is implemented in com/aerosimo/ominet/service/ErrorHospitalRESTService.java. It provides two endpoints:

- `POST /errorhospital/record`: Records a new error instance using a JSON payload.

Example Request:
```JSON
{
"faultcode": "XXX001",
"faultmessage": "User bad auth",
"faultservicename": "Web - Login"
}
```
Example Response:
```JSON
{
   "errorRef": "ERR|DA54BX2UM96MVAZY0TEWXMQWEB"
}
```
- `GET /errorhospital/top/{records}`: Fetches the top error records in JSON format based on the specified number.

Example Request:

```
GET http://localhost:8080/errorhospitalsystem/api/errorhospital/top/10
```
Example Response:
```JSON
[
   {
      "errorRef": "ERR|5R3F1RBEP0ZJ7338B1SAXKQVYW",
      "errorTime": "2024-10-19 03:03:34.196717",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|OQDG41AEMF36CQVQAYSTP5C3LG",
      "errorTime": "2024-10-19 03:03:31.895472",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|9PRUQMKZ58XUOR6F9DN9SXV524",
      "errorTime": "2024-10-19 03:03:29.373649",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|7P10ID0GUR5JN6JYKVSMRK2GFT",
      "errorTime": "2024-10-19 03:03:28.094253",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|3J3J3J2CXJV2FKPKJN2XBR0AZ4",
      "errorTime": "2024-10-19 03:03:27.119163",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|3C6TZ33CANTERHA002QI18IQG4",
      "errorTime": "2024-10-19 03:03:25.975949",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|HHVOSOG3JNWFA4YNMIY2NQB2AP",
      "errorTime": "2024-10-19 03:03:24.901855",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|DZHAOTYVVIK6VDDQOHQQ04YMG3",
      "errorTime": "2024-10-19 03:03:23.860691",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|O1HM2K458C8OKT6UDEXUS101C5",
      "errorTime": "2024-10-19 03:03:22.784929",
      "errorCode": "500",
      "errorMessage": "Internal Server Error",
      "errorService": "OrderService"
   },
   {
      "errorRef": "ERR|BBEGO9U7T86Q3ZB3PKK5AQ1FSS",
      "errorTime": "2024-10-18 21:00:25.164089",
      "errorCode": "00401",
      "errorMessage": "User bad auth",
      "errorService": "SOAPUI"
   }
]

```

---

![Aerosimo Logo.!](/img/logo.png "Aerosimo")