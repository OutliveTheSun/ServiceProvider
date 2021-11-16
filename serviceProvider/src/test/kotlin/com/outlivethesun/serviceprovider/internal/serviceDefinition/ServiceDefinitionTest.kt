package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.Service
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ServiceDefinitionTest {

    interface IService
    class ServiceWithParameter(parameter: String) : IService

    lateinit var serviceDefinition: ServiceDefinition<Any>

    @Test
    fun grabServiceTypeSingleInstanceable() {
        serviceDefinition = ServiceDefinition(IService::class, Service::class, ServiceInstanceType.SINGLE_INSTANCEABLE)
        assertNotNull(serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeSingleInstanceableWithInstance() {
        val service = Service()
        serviceDefinition =
            ServiceDefinition(IService::class, Service::class, ServiceInstanceType.SINGLE_INSTANCEABLE, service)
        assertEquals(service, serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeMultiInstanceable() {
        serviceDefinition = ServiceDefinition(IService::class, Service::class, ServiceInstanceType.MULTI_INSTANCEABLE)
        assertNotNull(serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeSingleInstanceableCached() {
        serviceDefinition = ServiceDefinition(IService::class, Service::class, ServiceInstanceType.SINGLE_INSTANCEABLE)
        assertEquals(serviceDefinition.grabService(), serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeMultiInstanceableCached() {
        serviceDefinition = ServiceDefinition(IService::class, Service::class, ServiceInstanceType.MULTI_INSTANCEABLE)
        assertNotEquals(serviceDefinition.grabService(), serviceDefinition.grabService())
    }

    @Test
    fun createServiceParameterExc() {
        serviceDefinition =
            ServiceDefinition(IService::class, ServiceWithParameter::class, ServiceInstanceType.SINGLE_INSTANCEABLE)
        try {
            serviceDefinition.grabService()
            fail()
        } catch (e: RuntimeException) {
        }
    }

}