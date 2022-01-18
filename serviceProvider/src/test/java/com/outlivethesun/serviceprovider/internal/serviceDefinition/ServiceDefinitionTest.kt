package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

internal class ServiceDefinitionTest {

    interface IServiceWithParameters
    class ServiceWithParameter(parameter: String) : IServiceWithParameters

    lateinit var serviceDefinition: ServiceDefinition<*>

    @Suppress("UNCHECKED_CAST")
    @Test
    fun fetchServiceTypeSingleInstanceable() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<IService>,
            ServiceInstanceType.SINGLE_INSTANCEABLE
        )
        assertNotNull(serviceDefinition.fetchService())
    }

    @Test
    fun fetchServiceTypeSingleInstanceableWithInstance() {
        val service = Service()
        serviceDefinition =
            ServiceDefinition(
                IService::class,
                Service::class as KClass<out IService>,
                ServiceInstanceType.SINGLE_INSTANCEABLE,
                service as IService
            )
        assertEquals(service, serviceDefinition.fetchService())
    }

    @Test
    fun fetchServiceTypeMultiInstanceable() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.MULTI_INSTANCEABLE
        )
        assertNotNull(serviceDefinition.fetchService())
    }

    @Test
    fun fetchServiceTypeSingleInstanceableCached() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.SINGLE_INSTANCEABLE
        )
        assertEquals(serviceDefinition.fetchService(), serviceDefinition.fetchService())
    }

    @Test
    fun fetchServiceTypeMultiInstanceableCached() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.MULTI_INSTANCEABLE
        )
        assertNotEquals(serviceDefinition.fetchService(), serviceDefinition.fetchService())
    }

    @Test
    fun createServiceParameterExc() {
        serviceDefinition =
            ServiceDefinition(IServiceWithParameters::class, ServiceWithParameter::class, ServiceInstanceType.SINGLE_INSTANCEABLE)
        try {
            serviceDefinition.fetchService()
            fail()
        } catch (e: RuntimeException) {
        }
    }

}