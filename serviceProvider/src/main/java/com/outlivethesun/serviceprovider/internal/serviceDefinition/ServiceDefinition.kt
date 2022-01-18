package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal data class ServiceDefinition<A : Any>(
    private val abstractServiceType: KClass<A>,
    private val concreteServiceType: KClass<out A>,
    private val serviceInstanceType: ServiceInstanceType,
    private var singleInstance: A? = null
) {

    fun fetchService(): A {
        return if (serviceInstanceType == ServiceInstanceType.SINGLE_INSTANCEABLE) {
            if (singleInstance == null) {
                singleInstance = createInstance()
            }
            singleInstance!!
        } else {
            createInstance()
        }
    }

    private fun createInstance(): A {
        try {
            return concreteServiceType.createInstance()
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Unable to create service \"${abstractServiceType.simpleName}\". The constructor of class \"${concreteServiceType.simpleName}\" must not have any parameters.")
        }
    }
}