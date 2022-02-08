package com.outlivethesun.serviceprovider.api

import kotlin.reflect.KClass

interface IServiceProvider {
    /**
     * Returns a cached service. Creates a service if it is not cached yet by autowiring or prior registration.
     * Throws an exception if a service cannot be created.
     */
    fun <A : Any> fetch(abstractServiceType: KClass<A>): A
    /**
     * Returns a cached service or null.
     */
    fun <A : Any> find(abstractServiceType: KClass<A>): A?

    /**
     * Adds an existing service to the cache to be fetched/found later.
     */
    fun <A : Any> put(abstractServiceType: KClass<A>, service: A)

    /**
     * Removes an existing service from the cache.
     */
    fun <A : Any> remove(abstractServiceType: KClass<A>)
    /**
     * Adds a definition of which concrete service [concreteServiceType] to be fetched when asked for a specific type [abstractServiceType].
     * ```
     * e.g.
     * ```
     * SP.register(IService:class, Service:class) -> SP.fetch(IService::class) : Service()
     *
     */
    fun <A : Any> register(
        abstractServiceType: KClass<A>,
        concreteServiceType: KClass<out A>,
        serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANCEABLE
    )
}

/**
 * Returns a cached service. Creates a service if it is not cached yet by autowiring or prior registration.
 * Throws an exception if a service cannot be created.
 */
inline fun <reified A : Any> IServiceProvider.fetch(): A {
    return this.fetch(A::class)
}

/**
 * Returns a cached service or null.
 */
inline fun <reified A : Any> IServiceProvider.find(): A? {
    return this.find(A::class)
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

/**
 * Removes an existing service from the cache.
 */
inline fun <reified A : Any> IServiceProvider.remove() {
    this.remove(A::class)
}

/**
 * Adds a definition of which concrete service [C] to be fetched when asked for a specific type [A].
 * ```
 * e.g.
 * ```
 * SP.register<IService, Service>() -> SP.fetch<IService> : Service()
 *
 */
inline fun <reified A : Any, reified C : A> IServiceProvider.register(serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANCEABLE) {
    this.register(A::class, C::class, serviceInstanceType)
}