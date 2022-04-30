package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.Service
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import com.outlivethesun.serviceprovider.internal.serviceRequest.IServiceRequest
import com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker.ITypeFetchingTracker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

internal class ServiceDefinitionTest {

    private var mockServiceRequest: IServiceRequest = mockk()
    private var mockServiceProvider = mockk<ServiceProvider>()
    private var mockTypeFetchingTracker: ITypeFetchingTracker = mockk(relaxed = true)

    init {
        every { mockServiceRequest.typeFetchingTracker } returns mockTypeFetchingTracker
        every { mockServiceRequest.serviceProvider } returns mockServiceProvider
    }

    interface IService1ToBeAddedAsParameter
    interface IService2ToBeAddedAsParameter
//    class ServiceToBeAddedAsParameter : IService1ToBeAddedAsParameter, IService2ToBeAddedAsParameter
    class ServiceWithParameter(val parameterService: IService1ToBeAddedAsParameter)
    class ServiceWithParameters(
        val parameterService1: IService1ToBeAddedAsParameter,
        val parameterService2: IService2ToBeAddedAsParameter
    )
    interface IServiceWithStringParameter

    @Suppress("UNUSED_PARAMETER")
    class ServiceWithStringParameter(parameter: String) : IServiceWithStringParameter

    private lateinit var testObject: ServiceDefinition<*>

    @Test
    fun fetchServiceTypeSingleInstantiable() {
        testObject = ServiceDefinition(
            Service::class,
            ServiceInstanceType.SINGLE_INSTANTIABLE
        )
        assertNotNull(testObject.fetchService(mockServiceRequest))
    }

    @Test
    fun fetchServiceTypeSingleInstantiableWithInstance() {
        val service = Service()
        testObject =
            ServiceDefinition(
                Service::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE,
                service as IService
            )
        assertEquals(service, testObject.fetchService(mockServiceRequest))
    }

    @Test
    fun fetchServiceTypeMultiInstantiable() {
        testObject = ServiceDefinition(
            Service::class,
            ServiceInstanceType.MULTI_INSTANTIABLE
        )
        assertNotNull(testObject.fetchService(mockServiceRequest))
    }

    @Test
    fun fetchServiceTypeSingleInstantiableCached() {
        testObject = ServiceDefinition(
            Service::class,
            ServiceInstanceType.SINGLE_INSTANTIABLE
        )
        assertEquals(
            testObject.fetchService(mockServiceRequest),
            testObject.fetchService(mockServiceRequest)
        )
    }

    @Test
    fun fetchServiceTypeMultiInstantiableNotCached() {
        testObject = ServiceDefinition(
            Service::class,
            ServiceInstanceType.MULTI_INSTANTIABLE
        )
        assertNotEquals(
            testObject.fetchService(mockServiceRequest),
            testObject.fetchService(mockServiceRequest)
        )
    }

    @Test
    fun createServiceWithStringParameterExc() {
        testObject =
            ServiceDefinition(
                ServiceWithStringParameter::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        try {
            testObject.fetchService(mockServiceRequest)
            fail()
        } catch (e: RuntimeException) {
        }
    }

    @Test
    fun createServiceWithParameter() {
        val mockServiceParameter = mockk<IService1ToBeAddedAsParameter>()
        every { mockServiceProvider.fetch(IService1ToBeAddedAsParameter::class, mockServiceRequest) } returns mockServiceParameter
        testObject =
            ServiceDefinition(
                ServiceWithParameter::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        val service =
            testObject.fetchService(mockServiceRequest) as ServiceWithParameter
        assertNotNull(service)
        assertEquals(mockServiceParameter, service.parameterService)
    }

    @Test
    fun createServiceWithParameters() {
        val mockServiceParameter1 = mockk<IService1ToBeAddedAsParameter>()
        val mockServiceParameter2 = mockk<IService2ToBeAddedAsParameter>()
        every { mockServiceProvider.fetch(IService1ToBeAddedAsParameter::class, mockServiceRequest) } returns mockServiceParameter1
        every { mockServiceProvider.fetch(IService2ToBeAddedAsParameter::class, mockServiceRequest) } returns mockServiceParameter2
        testObject =
            ServiceDefinition(
                ServiceWithParameters::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        val service = testObject.fetchService(mockServiceRequest) as ServiceWithParameters
        assertNotNull(service)
        assertEquals(mockServiceParameter1, service.parameterService1)
        assertEquals(mockServiceParameter2, service.parameterService2)
    }
}