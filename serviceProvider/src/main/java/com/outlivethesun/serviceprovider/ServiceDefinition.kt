package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

data class ServiceDefinition<A>(
    private val concreteServiceType: KClass<out Any>,
    private val serviceInstanceType: ServiceInstanceType,
    private var singleInstance: Any? = null
) {

    fun createService(): A {
        return if (serviceInstanceType == ServiceInstanceType.SINGLE_INSTANCEABLE) {
            if (singleInstance == null) {
                singleInstance = concreteServiceType.createInstance()
            }
            singleInstance as A
        } else {
            concreteServiceType.createInstance() as A
        }
    }
}