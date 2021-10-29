# IDTX Challenge

## Run the application

### Prerequisites

* Java JDK 11
* Apache Maven 3
* GIT

### Steps

````
git clone https://github.com/mmondino/idtx-challenge.git
cd idtx-challenge
./mvnw spring-boot:run
````

### Invocations from terminal

Test 1:
````
curl -i "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&pricingDate=2020-06-14T10:00:00"
````

Test 2:
````
curl -i "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&pricingDate=2020-06-14T16:00:00"
````

Test 3:
````
curl -i "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&pricingDate=2020-06-14T21:00:00"
````

Test 4:
````
curl -i "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&pricingDate=2020-06-15T10:00:00"
````

Test 5:
````
curl -i "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&pricingDate=2020-06-16T21:00:00"
````
## Junit test

[src/test/java/io/idtx/pricing/api/challenge/GetPriceIT.java](src/test/java/io/idtx/pricing/api/challenge/GetPriceIT.java)

## OpenApi 3
http://localhost:8080/swagger-ui.html

## HTTP status codes

### Successful requests
For successful requests, this API returns HTTP 2XX status codes.

<table>
  <thead>
    <tr>
      <th style="text-align: left;">Status code</th>
      <th style="text-align: left;">Description</th> 
    </tr>
  </thead>
  <tbody>
    <tr style="vertical-align: top;">
      <td>200 OK</td>
      <td>
        The request succeeded.
      </td>
    </tr>
  </tbody>
</table>

### Failed requests
For failed requests, this API returns HTTP 4XX status codes if something passed in the request has an error or 5XX status codes when something is wrong on our end with a server or service.

#### HTTP 4XX status codes

<table>
  <thead>
    <tr>
      <th style="text-align: left;">Status code</th>
      <th style="text-align: left;">Description</th>
      <th style="text-align: left;">Possible causes and solutions</th>
    </tr>
  </thead>
  <tbody>
    <tr style="vertical-align: top;">
      <td>400 Bad Request</td>
      <td>
        INVALID_REQUEST. Malformed request syntax, request is syntactically incorrect, or 
        violates a schema.
      </td>
      <td>
        See <a href="#invalid-requests">Invalid requests</a>
      </td>
    </tr>
    <tr style="vertical-align: top;">
      <td>
        404 Not Found
      </td>
      <td>
        RESOURCE_NOT_FOUND. The specified resource does not exist.
      </td>
      <td>
        The specified resource does not exist. The server did not find anything that matches
        the request URI. Either the URI is incorrect or the resource is not available. 
        For example, no price exists in the database matching the given filters parameters.<br/>
        <a href="#price-not-found">See an example response</a>
      </td>
    </tr>
    <tr style="vertical-align: top;">
      <td>
        405 Method Not Allowed
      </td>
      <td>
        METHOD_NOT_SUPPORTED
      </td>
      <td>
        The server does not implement the requested HTTP method. The service does not support the 
        requested HTTP method. For example, POST /api/v1/prices.
      </td>
    </tr>
    <tr style="vertical-align: top;">
      <td>406 Not Acceptable</td>
      <td>MEDIA_TYPE_NOT_ACCEPTABLE</td>
      <td>
        The server does not implement the media type that would be acceptable to the client. The server 
        cannot use the client-request media type to return the response payload. For example, 
        this error occurs if the client sends an Accept: application/xml request header because the API 
        can generate only an application/json response.
      </td>
    </tr>
    <tr style="vertical-align: top;">
      <td>415 Unsupported Media Type</td>
      <td>UNSUPPORTED_MEDIA_TYPE</td>
      <td>
        The server does not support the request payload’s media type. The API cannot process the media type of the 
        request payload. For example, this error occurs if the client sends a Content-Type: application/xml request 
        header because the API can only accept application/json request payloads.
      </td>
    </tr>
    <tr style="vertical-align: top;">
      <td>422 Unprocessable Entity</td>
      <td>UNPROCESSABLE_ENTITY</td>
      <td>
        The API cannot complete the requested action, or the request action is semantically incorrect or fails 
        business validation. The API cannot complete the requested action and might require interaction with APIs 
        or processes outside of the current request. For example, this error occurs for any business validation 
        errors, including errors that are not usually of the 400 type. Too many prices found is one of this errors.<br/>
        <a href="#too-many-prices-found">See an example response</a>
      </td>
    </tr>
  </tbody>
</table>

#### HTTP 5XX status codes

<table>
  <thead>
    <tr>
      <th style="text-align: left;">Status code</th>
      <th style="text-align: left;">Description</th>
      <th style="text-align: left;">Possible causes and solutions</th>
    </tr>
  </thead>
  <tbody>
    <tr style="vertical-align: top;">
      <td>500 Internal Server Error</td>
      <td>
        INTERNAL_SERVER_ERROR. An internal server error has occurred.
      </td>
      <td>
        A system or application error occurred. Although the client appears to provide a correct request, 
        something unexpected occurred on the server.
      </td>
    </tr>
    <tr style="vertical-align: top;">
      <td>503 Service Unavailable</td>
      <td>
        SERVICE_UNAVAILABLE. Service Unavailable.
      </td>
      <td>
        The server cannot handle the request for a service due to temporary maintenance.
      </td>
    </tr>
  </tbody>
</table>

## Invalid requests <a id="invalid-requests"></a>

Request is invalid when:
* The API cannot convert the request payload data to the underlying data type.<br>
* The data is not in the expected format.<br>
* A mandatory field is not present.<br>
* A data validation error occurred.

For invalid requests, this API returns the HTTP 400 Bad Request status code.

### Field error types

#### Conversion error
The API cannot convert the payload data to the underlying data type. 

##### Conversion error sample response

````
{
  "timestamp": "2021-10-27T17:57:55.706017Z",
  "problemType": "INVALID_REQUEST",
  "problemCode": null,
  "message": "The request is not valid",
  "globalErrors": [],
  "fieldErrors": [
    {
      "path": "brandId",
      "errorType": "CONVERSION_ERROR",
      "errorMessage": "expected long number",
      "rejectedValue": "A"
    },
    {
      "path": "productId",
      "errorType": "CONVERSION_ERROR",
      "errorMessage": "expected long number",
      "rejectedValue": "B"
    },
    {
      "path": "pricingDate",
      "errorType": "CONVERSION_ERROR",
      "errorMessage": "expected datetime format is yyyy-MM-ddTHH:mm:ss",
      "rejectedValue": "C"
    }
  ]
}
````

#### Validation error
The data is not in the expected data format, a required field is not available or a simple data validation error occurred  

##### Validation error sample response

````
{
  "timestamp": "2021-10-27T17:58:33.431137Z",
  "problemType": "INVALID_REQUEST",
  "problemCode": null,
  "message": "The request is not valid",
  "globalErrors": [],
  "fieldErrors": [
    {
      "path": "brandId",
      "errorType": "VALIDATION_ERROR",
      "errorMessage": "may not be null",
      "rejectedValue": null
    },
    {
      "path": "productId",
      "errorType": "VALIDATION_ERROR",
      "errorMessage": "may not be null",
      "rejectedValue": null
    },
    {
      "path": "pricingDate",
      "errorType": "VALIDATION_ERROR",
      "errorMessage": "may not be null",
      "rejectedValue": null
    }
  ]
}
````

## Price not found response <a id="price-not-found"></a>

````
{
  "timestamp": "2021-10-27T18:12:56.313540Z",
  "problemType": "RESOURCE_NOT_FOUND",
  "problemCode": "PRICES.PRICE_NOT_FOUND",
  "message": "Price not found"
}
````

## Too many prices found response <a id="too-many-prices-found"></a>

````
{
  "timestamp": "2021-10-27T18:10:50.999559Z",
  "problemType": "BUSINESS_RULE_PROBLEM",
  "problemCode": "PRICES.TOO_MANY_PRICES_FOUND",
  "message": "Too many prices found. Ambiguous price"
}
````

## Price found response <a id="price-found"></a>

````
{
  "validFrom": "2020-06-14T00:00:00",
  "validTo": "2020-12-31T23:59:59",
  "brandId": 1,
  "productId": 35455,
  "priceListId": 1,
  "amount": "35.50",
  "currency": "EUR"
}
````