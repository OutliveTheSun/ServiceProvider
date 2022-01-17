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

    @Test
    fun grabServiceTypeSingleInstanceable() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<IService>,
            ServiceInstanceType.SINGLE_INSTANCEABLE
        )
        assertNotNull(serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeSingleInstanceableWithInstance() {
        val service = Service()
        serviceDefinition =
            ServiceDefinition(
                IService::class,
                Service::class as KClass<out IService>,
                ServiceInstanceType.SINGLE_INSTANCEABLE,
                service as IService
            )
        assertEquals(service, serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeMultiInstanceable() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.MULTI_INSTANCEABLE
        )
        assertNotNull(serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeSingleInstanceableCached() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.SINGLE_INSTANCEABLE
        )
        assertEquals(serviceDefinition.grabService(), serviceDefinition.grabService())
    }

    @Test
    fun grabServiceTypeMultiInstanceableCached() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.MULTI_INSTANCEABLE
        )
        assertNotEquals(serviceDefinition.grabService(), serviceDefinition.grabService())
    }

    @Test
    fun createServiceParameterExc() {
        serviceDefinition =
            ServiceDefinition(IServiceWithParameters::class, ServiceWithParameter::class, ServiceInstanceType.SINGLE_INSTANCEABLE)
        try {
            serviceDefinition.grabService()
            fail()
        } catch (e: RuntimeException) {
        }
    }

}