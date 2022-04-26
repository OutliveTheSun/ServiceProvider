package com.outlivethesun.serviceprovider.api.exceptions

open class ServiceProviderException(override val message: String?, override val cause: Throwable? = null) :
    RuntimeException()