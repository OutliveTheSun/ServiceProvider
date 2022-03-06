package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

internal class ServiceDefinitionTest {

    interface IService1ToBeAddedAsParameter
    interface IService2ToBeAddedAsParameter
    class ServiceToBeAddedAsParameter : IService1ToBeAddedAsParameter, IService2ToBeAddedAsParameter
    interface IServiceWithParameter
    class ServiceWithParameter(val parameterService: IService1ToBeAddedAsParameter) : IServiceWithParameter
    interface IServiceWithParameters
    class ServiceWithParameters(
        val parameterService1: IService1ToBeAddedAsParameter,
        val parameterService2: IService2ToBeAddedAsParameter
    ) : IServiceWithParameters

    interface IServiceWithStringParameter
    class ServiceWithStringParameter(parameter: String) : IServiceWithStringParameter

    private lateinit var serviceDefinition: ServiceDefinition<*>

    @Suppress("UNCHECKED_CAST")
    @Test
    fun fetchServiceTypeSingleInstantiable() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<IService>,
            ServiceInstanceType.SINGLE_INSTANCEABLE
        )
        assertNotNull(serviceDefinition.fetchService())
    }

    @Test
    fun fetchServiceTypeSingleInstantiableWithInstance() {
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
    fun fetchServiceTypeMultiInstantiable() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.MULTI_INSTANCEABLE
        )
        assertNotNull(serviceDefinition.fetchService())
    }

    @Test
    fun fetchServiceTypeSingleInstantiableCached() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.SINGLE_INSTANCEABLE
        )
        assertEquals(serviceDefinition.fetchService(), serviceDefinition.fetchService())
    }

    @Test
    fun fetchServiceTypeMultiInstantiableCached() {
        serviceDefinition = ServiceDefinition(
            IService::class,
            Service::class as KClass<out IService>,
            ServiceInstanceType.MULTI_INSTANCEABLE
        )
        assertNotEquals(serviceDefinition.fetchService(), serviceDefinition.fetchService())
    }

    @Test
    fun createServiceWithStringParameterExc() {
        serviceDefinition =
            ServiceDefinition(
                IServiceWithStringParameter::class,
                ServiceWithStringParameter::class,
                ServiceInstanceType.SINGLE_INSTANCEABLE
            )
        try {
            serviceDefinition.fetchService()
            fail()
        } catch (e: RuntimeException) {
        }
    }

    @Test
    fun createServiceWithParameter() {
        serviceDefinition =
            ServiceDefinition(
                IServiceWithParameter::class,
                ServiceWithParameter::class,
                ServiceInstanceType.SINGLE_INSTANCEABLE
            )
        val service = serviceDefinition.fetchService() as ServiceWithParameter
        assertNotNull(service)
        assertNotNull(service.parameterService)
    }

    @Test
    fun createServiceWithParameters() {
        serviceDefinition =
            ServiceDefinition(
                IServiceWithParameters::class,
                ServiceWithParameters::class,
                ServiceInstanceType.SINGLE_INSTANCEABLE
            )
        assertNotNull(serviceDefinition.fetchService())
    }
}