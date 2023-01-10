## RAT Web Service with an Asynchronous Client
In this folder, there are twoimplementations for the RAT webservice. The first one uses OpenAPI to expose an interface that the asynchronous client would leverage to make requests, whereas the second one uses XML Soap.


### Technology Enablers
#### OpenAPI
- **WSDL (Web Service Definition Language)**: WSDL is an XML based web service definition language. It is used to describe the API that will be used to both generate the stubs and skeletons at the level of the consumer and service provider.
- **SOAP over HTTP**: is an XML based protocol that we use to write the body of the POST http requests that we make between the consumer and service provider and vice-versa.
- **Java**: The programming language of the service provider.
- **JAX-WS API**: stands for Java XML Web Services API. In our project, it allowed us to mark our service through the JAX-WS @WebService annotation. Also, it provided the wsgen command to create the WSDL file and generate the skeletons for the service provider.
- **JavaScript**: The programming language for the consumer.
- **Soap-as-promised**: a JavaScript library that was used to generate the client’s stubs to use the RAT web service, from a WSDL file. The library allows us to use promises.

#### XML Soap
- **OpenAPI Specification 3.0 (OAS 3.0)**: The service definition language
- Raw HTTP: The protocol used for communication between the service provider and the consumer.
- **JAVA**: The programming language of the service provider.
- **Spring Boot**: a java-based framework that we leveraged to develop and expose the remote access system web service.
- **JavaScript**: The programming language for the consumer.
- **Swagger-codegen*: a library that allowed us to generate the client stubs for the consumer from the OpenAPI Specification of our remote access control web service.

### Development Approach
#### OpenAPI
The development approach for this part of the project involved using the code-first (java-first) approach. This approach was chosen because it is difficult to manually generate a WSDL file, and the JAX-WS API was used to generate the file automatically. To begin, the code for the service provider was written and the class containing the functionalities of the provider was annotated with the @WebService annotation. The WSDL file and skeletons were then generated using the following wsgen command:
```
wsgen -wsdl -cp app/build/classes/java/main -d app/build/classes/java/main/ -r app/src/main/resources RATService.RAT
```

For the client, the soap-as-promised library was used to generate the client stub on the fly when calling the functions of the remote access control service. This library supports the use of promises, so a client promise was created with soap.createClient(url) and then specific functions were called with chained "thens". In the case of the three functionalities - getProcesses, takeScreenshot, and reboot - it was necessary to use Promise.all to ensure that the reboot function was called after the getProcesses and takeScreenshot promises had been resolved. An array of these two promises was passed to Promise.all and the call back function invoked the reboot function once they were both resolved.

#### XML Soap
The development approach for this part of the project involved using the code-first (java-first) approach. This approach was chosen because it is difficult to manually generate the OpenAPI service description, and the Springdoc-OpenAPI generation tool (org.springdoc:springdoc-openapi-ui:1.5.5) was used to automatically generate it.

For the client, the OpenAPI definition for the Calculator service was used to generate a stub with swagger-codgen. This stub was then installed using npm so that it could be used as a package in the project. A client was created and wrapped in a promise using Promise.resolve, and specific functions were called with chained "thens". In the case of the three functionalities - getProcesses, takeScreenshot, and reboot - it was necessary to use Promise.all to ensure that the reboot function was called after the getProcesses and takeScreenshot promises had been resolved. An array of these two promises was passed to Promise.all and the call back function invoked the reboot function once they were both resolved.