# forex-service-sample
Foreign Exchange Rate Service Example


## Project Overview
The forex-service-sample is a simple Java web application written using Spring Boot.
It exposes some RESTful services that return the conversion rate for Euro to any other currency.
The exchange rates are periodically updated from the European Central Bank web service.


## Quick start
In order to test easily the project, is needed to import the project using a compatible IDE (IntelliJ Idea, STS, Eclipse, etc.) and run the Application class.
Another possibility is to build the project using Maven and then run the resulting jar artifact with the command:
```
java -jar exchange-1.0.jar
```
The built artifact is provided also in the current repository.


## Configuration

It is possible to configure some parameters in the application.properties file:
```properties
ecb.webservice.url=http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml
update.task.rate=600000
```
- The parameter ecb.webservice.url is the URL of the European Central Bank webservice;
- update.task.rate specifies in milliseconds the rate of the update exchange rate task.


## Implementation

The forex-service-sample project is a web application written in Java using Spring Boot.
It uses an H2 database in order to store the data provided from European Central Bank.

### Project structure
The structure of the project is layered in order to separate different contexts.
Following the names of the sub-packages and their functions:

##### controller
contains the definition of the RESTful API.

##### data.domain
the bean that represent the stored data structure and also the JSON object structure used for the response.

##### data.repository
repository containg the queries definition that can be done on domain object.

##### data.xml
JAXB bean that represents the structure of the XML response given by ECB web services.

##### service
services that contain all the business logic of the application: data manipulation, ECB webservice queries.

##### task
asynchronous task that is run at regular intervals


## Call examples
Following some call examples of the RESTful API:

```
http://localhost:8080/api/exchangeRate/list
```
```
http://localhost:8080/api/exchangeRate/all?cur=USD
```
```
http://localhost:8080/api/exchangeRate/all/2017-05-02
```
```
http://localhost:8080/api/exchangeRate/2017-05-02?cur=USD
```
