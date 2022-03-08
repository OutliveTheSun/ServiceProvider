package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.Service
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.annotations.MultiInstantiable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

interface IMultiInstanceAnnotated

@MultiInstantiable
class MultiInstanceAnnotated : IMultiInstanceAnnotated

internal class ServiceDefinitionFactoryTest {

    private val serviceDefinitionFactory: IServiceDefinitionFactory = ServiceDefinitionFactory()

    @Test
    fun createByType() {
        assertNotNull(
            serviceDefinitionFactory.createByType<IService>(
                IService::class,
                Service::class
            )
        )
    }

    @Test
    fun createByTypeSingleInstantiable() {
        assertNotNull(
            serviceDefinitionFactory.createByType<IService>(
                IService::class,
                Service::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        )
    }

    @Test
    fun createByInstance() {
        assertNotNull(serviceDefinitionFactory.createByInstance(IService::class, Service()))
    }

    @Test
    fun createByMultiInstantiableInstance() {
        val serviceDefinition = serviceDefinitionFactory.createByInstance(
            IMultiInstanceAnnotated::class,
            MultiInstanceAnnotated()
        )
        assertNotEquals(
            serviceDefinition.fetchService(),
            serviceDefinition.fetchService()
        )
    }

    @Test
    fun createBySingleInstantiableInstance() {
        val serviceDefinition = serviceDefinitionFactory.createByInstance(
            IService::class,
            Service()
        )
        assertEquals(
            serviceDefinition.fetchService(),
            serviceDefinition.fetchService()
        )
    }
}