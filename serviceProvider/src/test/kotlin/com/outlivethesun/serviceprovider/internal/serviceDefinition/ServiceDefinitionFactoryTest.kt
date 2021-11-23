package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

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
    fun createByTypeSingleInstanceable() {
        assertNotNull(
            serviceDefinitionFactory.createByType<IService>(
                IService::class,
                Service::class,
                ServiceInstanceType.SINGLE_INSTANCEABLE
            )
        )
    }

    @Test
    fun createByInstance() {
        assertNotNull(serviceDefinitionFactory.createByInstance(IService::class, Service()))
    }
}