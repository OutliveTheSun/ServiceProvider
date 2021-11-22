package com.outlivethesun.serviceprovider.api

import kotlin.reflect.KClass

interface IServiceProvider {
    fun <A : Any> fetch(abstractServiceType: KClass<A>): A
    fun <A : Any> put(abstractServiceType: KClass<A>, service: A)
    fun <A : Any> remove(abstractServiceType: KClass<A>)
    fun <A : Any> register(
        abstractServiceType: KClass<A>,
        concreteServiceType: KClass<out A>,
        serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANCEABLE
    )
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

inline fun <reified A : Any, reified C : A> IServiceProvider.register(serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANCEABLE) {
    this.register(A::class, C::class, serviceInstanceType)
}