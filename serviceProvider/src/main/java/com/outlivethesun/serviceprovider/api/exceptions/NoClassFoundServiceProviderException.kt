package com.outlivethesun.serviceprovider.api.exceptions

import kotlin.reflect.KClass

class NoClassFoundServiceProviderException(abstractTypeToAutowire: KClass<out Any>) : AutowireServiceProviderException(
    abstractTypeToAutowire,
    "No classes found to autowire. Ensure exactly one class implements the interface '${abstractTypeToAutowire.simpleName}'."
)