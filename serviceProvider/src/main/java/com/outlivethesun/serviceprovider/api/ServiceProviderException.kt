package com.outlivethesun.serviceprovider.api

import kotlin.reflect.KClass

open class ServiceProviderException(override val message: String?) : RuntimeException()
class UnableToCreateServiceException(serviceKClass: KClass<out Any>, additionalMessage: String?) :
    ServiceProviderException("Unable to create instance of service '${serviceKClass.simpleName}'. $additionalMessage")