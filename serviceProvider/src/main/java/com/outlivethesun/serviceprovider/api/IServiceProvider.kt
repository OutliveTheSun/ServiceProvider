package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.api.exceptions.ServiceProviderException
import kotlin.reflect.KClass

/**
 * An implementation of this interface is responsible for loading, retrieving and administering services.
 */
interface IServiceProvider {
    /**
     * Returns a service of the given [abstractServiceType]. It either returns a cached service
     * or creates one by autowiring or registration.
     * Throws an exception of type [ServiceProviderException] if a service cannot be returned.
     */
    fun <T : Any> fetch(abstractServiceType: KClass<T>): T

    /**
     * Returns a cached service of the given [abstractServiceType]. Creates a service if it is not cached yet by autowiring or prior registration.
     * Returns null if there is no service to be autowired.
     */
    fun <T : Any> fetchOrNull(abstractServiceType: KClass<T>): T?

    /**
     * Returns a cached service of the given [abstractServiceType] or null if it is not found. The service is not created.
     */
    fun <T : Any> find(abstractServiceType: KClass<T>): T?

    /**
     * Adds an existing service of the given [abstractServiceType] to the cache to be fetched/found later.
     */
    fun <T : Any> put(abstractServiceType: KClass<T>, service: T)

    /**
     * Removes an existing service of the given [abstractServiceType] from the cache.
     */
    fun <T : Any> remove(abstractServiceType: KClass<T>)

    /**
     * Adds a definition of the concrete service of type [concreteServiceType] to be fetched when an [abstractServiceType] is requested.
     * The [serviceInstanceType] distinguishes whether a service can be created as singleton ([ServiceInstanceType.SINGLE_INSTANTIABLE])
     * or a new instance with each request ([ServiceInstanceType.MULTI_INSTANTIABLE]).
     * ```
     * e.g.
     * ```
     * SP.register(IService:class, Service:class) -> SP.fetch(IService::class) : Service()
     */
    fun <T : Any> register(
        abstractServiceType: KClass<T>,
        concreteServiceType: KClass<out T>,
        serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANTIABLE
    )
}

/**
 * Returns a service of the given generic type [T]. It either returns a cached service
 * or creates one by autowiring or registration.
 * Throws an exception of type [ServiceProviderException] if a service cannot be returned.
 */
inline fun <reified T : Any> IServiceProvider.fetch(): T {
    return this.fetch(T::class)
}

/**
 * Returns a cached service of the given generic type [T]. Creates a service if it is not cached yet by autowiring or prior registration.
 * Returns null if there is no service to be autowired.
 */
inline fun <reified T : Any> IServiceProvider.fetchOrNull(): T? {
    return this.fetchOrNull(T::class)
}

/**
 * Returns a cached service of the given generic type [T] or null if it is not found. The service is not created.
 */
inline fun <reified T : Any> IServiceProvider.find(): T? {
    return this.find(T::class)
}

/**
 * Adds an existing service of the given generic type [T] to the cache to be fetched/found later.
 */
inline fun <reified T : Any> IServiceProvider.put(service: T) {
    this.put(T::class, service)
}

/**
 * Removes an existing service of the given generic type [T] from the cache.
 */
inline fun <reified T : Any> IServiceProvider.remove() {
    this.remove(T::class)
}

/**
 * Adds a definition of the concrete service of type [C] to be fetched when a service of type [A] is requested.
 * The [serviceInstanceType] distinguishes whether a service can be created as singleton ([ServiceInstanceType.SINGLE_INSTANTIABLE])
 * or a new instance with each request ([ServiceInstanceType.MULTI_INSTANTIABLE]).
 * ```
 * e.g.
 * ```
 * SP.register<IService, Service>() -> SP.fetch<IService>() : Service()
 */
inline fun <reified A : Any, reified C : A> IServiceProvider.register(serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANTIABLE) {
    this.register(A::class, C::class, serviceInstanceType)
}