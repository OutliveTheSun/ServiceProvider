package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.annotations.MultiInstantiable
import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.Service
import com.outlivethesun.serviceprovider.internal.serviceRequest.IServiceRequest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

interface IMultiInstanceAnnotated

@MultiInstantiable
class MultiInstanceAnnotated : IMultiInstanceAnnotated

internal class ServiceDefinitionFactoryTest {

    private val testObject: IServiceDefinitionFactory = ServiceDefinitionFactory()
    private val mockServiceRequest = mockk<IServiceRequest>()

    @Test
    fun createByType() {
        assertNotNull(
            testObject.createByType<IService>(Service::class, ServiceInstanceType.SINGLE_INSTANTIABLE)
        )
    }

    @Test
    fun createByTypeSingleInstantiable() {
        assertNotNull(
            testObject.createByType<IService>(
                Service::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        )
    }

    @Test
    fun createByInstance() {
        assertNotNull(testObject.createByInstance(Service()))
    }

    @Test
    fun createByMultiInstantiableInstance() {
        val serviceDefinition = testObject.createByInstance(MultiInstanceAnnotated())
        assertNotEquals(
            serviceDefinition.fetchService(mockServiceRequest),
            serviceDefinition.fetchService(mockServiceRequest)
        )
    }

    @Test
    fun createBySingleInstantiableInstance() {
        val serviceDefinition = testObject.createByInstance(Service())
        assertEquals(
            serviceDefinition.fetchService(mockServiceRequest),
            serviceDefinition.fetchService(mockServiceRequest)
        )
    }
}