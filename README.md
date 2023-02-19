# ServiceProvider
A dependency injection implementation in Kotlin by using a service provider / service locator.

## Usage
The service provider is used to achieve independency from concrete classes and to write more flexible code. Instead of initializing concrete service classes, a service is requested from the service provider via an abstract service type, which is represented by an interface type.

At a central point the concrete services, which should be used in a session, have to be provided to the service provider either by setting initialized services or by providing the concrete service type class. When requesting a service the service provider initializes the services lazy on demand. 

After providing the services, these services can be fetched from the service provider. At that point there is no dependency anymore to concrete service implementations but instead to the abstract service type.

The following chapter shows how to provide and consume a service.

### Implement a service
At first a service has to be implemented which must implement an interface, which is the abstract service type through which the service can be addressed.

1. Implement an abstract service type in form of an interface.
```
interface ILogger {
    fun log(message: String)
}
```

2. Implement a variant of the the service itself.
```
class Logger : ILogger {
    override fun log(message: String) {
        println(message)
    }
}
```

### Provide a service to the service provider
To request a service from the service provider, it either has to be provided first, generally at a central place within an application or it can be created via autowiring.
There are two ways to provide a service which are explained below.

**Hint:** Providing an already existing service type would replace the current service.

1. Put a service 

*Put* is used to add an already initialized service to the service provider. The service is passed as parameter and is registered by its abstract service type at the service provider.

To put a service means it is handled as a singleton service. Each time that service is requested, the same instance will be returned.
```
val logger: ILogger = Logger()
SP.put(logger)
```

2. Register a service

Registering means to provide the abstract and concrete service types, without the need to initialize the service before. Instead the service provider will initialize the service lazily on demand. *Only services without constructor parameters or services as constructor parameters are supported.*

The two generic parameters representing the abstract service type and the concrete service type have to be set for registering.

Optionally the parameter *serviceInstanceType* can be passed as a parameter. It defines if a new service instance is created with each request or if the service is handled as singleton, which means once the service is initialized the same instance is returned with each request. As default the *serviceInstanceType* is considered to be *MULTI_INSTANTIABLE*.
```
SP.register<ILogger, Logger>()

SP.register<ILogger, Logger>(ServiceInstanceType.SINGLE_INSTANTIABLE)
```

### Request a service
Requesting a service is possible by providing its abstract service type to the methods *fetch* or *fetchOrNull*. As only the abstract service type is referenced, the caller has only a dependency to that service type. The concrete service class type is unknown. Below two examples are shown for fetching services:

1. Fetch

By using *fetch* the caller expects the service to be available. The service provider returns an instance of the service type or throws an exception if the service doesn't exist.
```
val logger = SP.fetch<ILogger>()
logger.log("Message to be logged")
```

2. FetchOrNull
 
*FetchOrNull* is equivalent to *fetch* but in case that the requested service doesn't exist, *null* is returned instead of throwing an exception.
```
val logger = SP.fetchOrNull<ILogger>()
logger?.log("Message to be logged")
```

### Remove a service
To remove a service from the service provider, pass the abstract service type as the generic type to *remove*.
```
SP.remove<ILogger>()
```

### Check if a service exist
The method *find* can be used to find out, if a service was registered at the service provider. It returns *null* if the service is not found or was not initialized.
```
SP.find<ILogger>()
```
