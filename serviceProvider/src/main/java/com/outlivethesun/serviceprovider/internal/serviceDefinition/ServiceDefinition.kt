@file:Suppress("UNCHECKED_CAST")

package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal data class ServiceDefinition<A>(
    private val abstractServiceType: KClass<out Any>,
    private val concreteServiceType: KClass<out Any>,
    private val serviceInstanceType: ServiceInstanceType,
    private var singleInstance: A? = null
) {

    fun grabService(): A {
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
            return concreteServiceType.createInstance() as A
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Unable to create service \"${abstractServiceType.simpleName}\". The constructor of class \"${concreteServiceType.simpleName}\" must not have any parameters.")
        }
    }
}