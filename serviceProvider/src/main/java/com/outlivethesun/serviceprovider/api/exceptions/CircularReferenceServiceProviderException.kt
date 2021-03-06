package com.outlivethesun.serviceprovider.api.exceptions

import kotlin.reflect.KClass

class CircularReferenceServiceProviderException(val abstractTypeToAutowire: KClass<out Any>) :
    AutowireServiceProviderException(
        abstractTypeToAutowire,
        "Circular reference exists. Consider registering the service '${abstractTypeToAutowire.simpleName}' manually."
    )