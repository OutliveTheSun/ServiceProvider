package com.outlivethesun.serviceprovider.internal

import com.outlivethesun.reflectioninfo.IReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.serviceprovider.api.IServiceProvider
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import com.outlivethesun.serviceprovider.api.exceptions.NoClassFoundAutowireException
import com.outlivethesun.serviceprovider.api.put
import com.outlivethesun.serviceprovider.internal.serviceDefinition.IServiceDefinition
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionDictionary
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionFactory
import com.outlivethesun.serviceprovider.internal.serviceRequest.IServiceRequest
import com.outlivethesun.serviceprovider.internal.serviceRequest.ServiceRequest
import kotlin.reflect.KClass

internal class ServiceProvider : IServiceProvider {
    private val reflectionInfo by lazy { bootLoadReflectionInfo() }
    private val serviceDefinitionFactory by lazy { ServiceDefinitionFactory() }
    private val serviceDefinitionDictionary by lazy {
        ServiceDefinitionDictionary(
            serviceDefinitionFactory,
            reflectionInfo
        )
    }
    private val serviceDefinitions = mutableMapOf<KClass<*>, IServiceDefinition<*>>()

    override fun <T : Any> fetch(abstractServiceType: KClass<T>): T {
        return fetch(abstractServiceType, ServiceRequest(this, abstractServiceType))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> fetch(abstractServiceType: KClass<T>, serviceRequest: IServiceRequest): T {
        checkForCircularReference(serviceRequest, abstractServiceType)
        val service: Any
        serviceRequest.typeFetchingTracker.addType(abstractServiceType)
        val serviceDefinition =
            serviceDefinitions[abstractServiceType] ?: fetchServiceDefinitionFromDictionary(abstractServiceType)
        service = serviceDefinition.fetchService(serviceRequest)
        return service as T
    }

    /**
     * Check if the [abstractServiceType] is already being requested. Requesting it twice within the same
     * fetch request would result in an infinite loop. In this case a [CircularReferenceServiceProviderException] is raised.
     */
    private fun <T : Any> checkForCircularReference(
        serviceRequest: IServiceRequest,
        abstractServiceType: KClass<T>
    ) {
        serviceRequest.typeFetchingTracker.checkTypeIsNotTracked(abstractServiceType)
    }

    override fun <T : Any> fetchOrNull(abstractServiceType: KClass<T>): T? {
        return try {
            SP.fetch(abstractServiceType)
        } catch (e: NoClassFoundAutowireException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> find(abstractServiceType: KClass<T>): T? {
        return serviceDefinitions[abstractServiceType]?.let {
            val serviceRequest = ServiceRequest(this, abstractServiceType)
            serviceRequest.typeFetchingTracker.addType(abstractServiceType)
            it.fetchService(serviceRequest) as T
        }
    }

    override fun <T : Any> put(abstractServiceType: KClass<T>, service: T) {
        serviceDefinitions[abstractServiceType] =
            serviceDefinitionFactory.createByInstance(service)
    }

    override fun <T : Any> remove(abstractServiceType: KClass<T>) {
        serviceDefinitions.remove(abstractServiceType)
    }

    override fun <T : Any> register(
        abstractServiceType: KClass<T>,
        concreteServiceType: KClass<out T>,
        serviceInstanceType: ServiceInstanceType
    ) {
        serviceDefinitions[abstractServiceType] =
            serviceDefinitionFactory.createByType(
                concreteServiceType,
                serviceInstanceType
            )
    }

    private fun <T : Any> fetchServiceDefinitionFromDictionary(abstractServiceType: KClass<T>): IServiceDefinition<T> {
        return serviceDefinitionDictionary.fetch(abstractServiceType).apply {
            serviceDefinitions[abstractServiceType] = this
        }
    }

    private fun bootLoadReflectionInfo(): IReflectionInfo {
        return ReflectionInfo().apply { put<IReflectionInfo>(this) }
    }
}