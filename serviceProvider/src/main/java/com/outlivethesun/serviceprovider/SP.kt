package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass

object SP : IServiceProvider {

    private val services = mutableMapOf<KClass<*>, Any>()

    override fun <T : Any> getService(serviceType: KClass<T>): T {
        val service = services[serviceType]
        return if (service != null) {
            service as T
        } else {
            when (serviceType) {
                else -> throw NotImplementedError()
            }
        }
    }

    override fun <T : Any> setService(serviceType: KClass<T>, service: T) {
        services.put(serviceType, service)
    }
}