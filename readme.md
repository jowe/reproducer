
Reproducer code to show that since payara 4.1.2.182 and 5.182
which introduces opentracing module interferes with application provided
JAX-RS exception mappers. 


# Description

Application provided REST ExceptionMapper ist not called aynmore (perhaps due to new OpenTracing module)

We use an ExceptionMapper<Throwable> and we see since 5.182/4.1.2.182 that this mapper
isn't called any more, which breaks our complete Error/Exceptionhandling.

Looking at the Payara Code Repo we see a new class: 
JaxrsContainerRequestTracingExceptionMapper implements ExceptionMapper<Throwable>
which may intefere with application exception mappers.

JAX-RS does not specify the semantic if several Mappers are available for the same type,
but I think that OpenTracing should not intefere how an application maps exceptions to Responses.


## Expected Outcome

If we define an ExceptionMapper ist must be called and exception must not be mapped
and by an internal Mapper.

 
## Current Outcome
Since the application provided exception mapper and the JaxrsContainerRequestTracingExceptionMapper
compete, JAX-RS/Jersey will only choose one (randomly) - during CDI statrup.

So we see correct and wrong behaviours:

### Correct
**Behaviour:** Our ExceptionMapper is called.

**Output:**
 ```
AppMapperCalled
 ``` 
 
**Log:**
 ```
 [2018-09-24T08:25:28.699+0200] [Payara 5.183] [SEVERE] [] [fish.payara.microprofile.opentracing.jaxrs.JaxrsContainerRequestTracingFilter] [tid: _ThreadID=38 _ThreadName=http-thread-pool::http-listener-1(3)] [timeMillis: 1537770328699] [levelValue: 1000] [[
   java.lang.RuntimeException: my test exception
 java.lang.RuntimeException: my test exception
 	at payara.reproducer.RestResource.throwsException(RestResource.java:13)
 ```


### Wrong
**Behaviour:** Our ExceptionMapper is _not_ called.

**Output:**
 ```
java.lang.RuntimeException: my test exception
 ``` 
 
**Log:**
 ```
[2018-09-24T08:24:20.008+0200] [Payara 5.183] [SEVERE] [] [payara.reproducer.AppExceptionMapper] [tid: _ThreadID=36 _ThreadName=http-thread-pool::http-listener-1(2)] [timeMillis: 1537770260008] [levelValue: 1000] [[
  AppExceptionMapper was called
java.lang.RuntimeException: my test exception
	at payara.reproducer.RestResource.throwsException(RestResource.java:13)
```


## Steps to reproduce (Only for bug reports) 
Deplyoy reproducer.war

Call: http://localhost:8080/reproducer/rest/throwex

Since the behaviour is random your have to restart your damain to see both behaviours.


## Environment ##

- **Payara Version**: since 5.182/4.1.2.182
- **Edition**:  Full
- **JDK Version**: 8 OpenJDK 
- **Operating System**:  Linux 
- **Database**: Oracle



