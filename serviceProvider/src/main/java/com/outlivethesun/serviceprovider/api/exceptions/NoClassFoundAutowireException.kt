package com.outlivethesun.serviceprovider.api.exceptions

import kotlin.reflect.KClass

open class NoClassFoundAutowireException(
    abstractTypeToAutowire: KClass<out Any>,
    additionalMessage: String? = "Ensure exactly one class implements the interface '${abstractTypeToAutowire.simpleName}'."
) : AutowireServiceProviderException(
    abstractTypeToAutowire,
    "No classes found to autowire.${additionalMessage?.let { " $it" }}."
)