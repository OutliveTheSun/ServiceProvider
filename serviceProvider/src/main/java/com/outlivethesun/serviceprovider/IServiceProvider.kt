package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass

interface IServiceProvider {
    fun <A : Any> fetch(abstractServiceType: KClass<out A>): A
    fun <A : Any> put(abstractServiceType: KClass<out A>, service: A)
    fun <A : Any> remove(abstractServiceType: KClass<out A>)
}

inline fun <reified A : Any> IServiceProvider.fetch(): A {
    return this.fetch(A::class)
}

/**
 * Sets an existing service instance.
 * For registering the service under its abstract type, this type has to be provided by the generics type
 * e.g.:
 * setService<IAbstractServiceName>(concreteServiceInstance)
 */
inline fun <reified A : Any> IServiceProvider.put(service: A) {
    this.put(A::class, service)
}

inline fun <reified A : Any> IServiceProvider.remove() {
    this.remove(A::class)
}