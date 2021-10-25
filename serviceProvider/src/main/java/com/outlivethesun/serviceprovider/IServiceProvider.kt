package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass

interface IServiceProvider {
    fun <T: Any> getService(serviceType: KClass<T>): T
    fun <T: Any> setService(serviceType: KClass<T>, service: T)
}