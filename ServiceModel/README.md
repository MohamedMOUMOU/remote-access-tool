## RAT Web Service

### Interface (API)

The interface defines three functions:

1. Get Processes
    - Name of the function: getProcesses
    - Arguments and their types: None
    - Return value and its type: processes of the remote system (string)

2. Take a Screenshot
    - Name of the function: takeScreenshot
    - Arguments and their types: screenshot name (string)
    - Return value and its type: content of the file (array of bytes)

3. Reboot the Remote System
    - Name of the function: reboot
    - Arguments and their types: waiting time to reboot (integer)
    - Return value and its type: confirmation of command execution (string)

### Technology Enablers
- **WSDL (Web Service Definition Language)**: WSDL is an XML based web service definition language. It is used to describe the API that will be used to both generate the stubs and skeletons at the level of the consumer and service provider.
- **SOAP over HTTP**: is an XML based protocol that we use to write the body of the POST http requests that we make between the consumer and the provider and vice-versa.
- **Java**: The programming language of the service provider with its libraries that enabled us to build the functionalities of the service provider, and most importantly, the javax.jws library that allowed us to annotate the RAT class with @WebService annotation.
- **JAX-WS API**: stands for Java XML Web Services API, which allows us to create web services. In our project, it allowed us to use the wsgen command that enabled us to create the WSDL file and generate the skeletons for the service provider.
- **Python**: The programming language for the consumer.
- **Zeep**: a python library that is used to generate the appropriate code (the stubs), to use a web service, from a WSDL file.

### Description of the Communication Between the Service Consumer and the Service Provider

1. The service consumer uses the library Zeep to read the WSDL file and generate the stubs dynamically.
2. The service consumer calls a function as if it were local.
3. When the function is called, an http post request is sent with an XML/SOAP body containing the parameters needed by the function.
4. The parameters are marshalled before the request is sent.
5. The service provider receives the request, unmarshalls the parameters, and calls the specific function and pass to it the parameters.
6. The service provider then sends a http reply with and XML SOAP body, containing the value returned by the function.
7. The returned value is marshalled before the request is sent.
8. The client receives the response and unmarshalls the returned value.

### Development Approach
The code-first approach was used for this project because it is difficult to generate a WSDL file manually and the JAX-WS API can be used to generate it. The service provider code was written first, with the class containing the service provider's functionality annotated with @WebService. The WSDL file and skeletons were then generated using the following wsgen command:

``` 
wsgen -wsdl -cp app/build/classes/java/main -d app/build/classes/java/main/ -r app/src/main/resources  RATService.RAT 
```

For the client, knowledge of the functions to be called, their required variables and their types, and their return values is necessary. As the developers of the provider, this information is already known without consulting the WSDL file. However, in other cases, the WSDL file would need to be read to obtain this information. The Zeep library was used to dynamically generate stubs from the WSDL file and call the RAT service provider.