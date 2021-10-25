package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass

interface IServiceProvider {
    fun <A : Any> getService(abstractServiceType: KClass<out A>): A
    fun <A : Any> setService(abstractServiceType: KClass<out A>, service: A)
}

inline fun <reified A : Any> IServiceProvider.getService(): A {
    return this.getService(A::class)
}

inline fun <reified A : Any> IServiceProvider.setService(service: A) {
    this.setService(A::class, service)
}