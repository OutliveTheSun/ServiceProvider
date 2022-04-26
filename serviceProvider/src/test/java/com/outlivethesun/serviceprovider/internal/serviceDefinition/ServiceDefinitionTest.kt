package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.Service
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
    private var mockTypeFetchingTracker: ITypeFetchingTracker = mockk(relaxed = true)

    init {
        every { mockServiceRequest.typeFetchingTracker } returns mockTypeFetchingTracker
    }

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
    fun fetchServiceTypeMultiInstantiableCached() {
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
        testObject =
            ServiceDefinition(
                ServiceWithParameter::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        val service =
            testObject.fetchService(mockServiceRequest) as ServiceWithParameter
        assertNotNull(service)
        assertNotNull(service.parameterService)
        verify { mockServiceRequest.typeFetchingTracker }
    }

    @Test
    fun createServiceWithParameters() {
        testObject =
            ServiceDefinition(
                ServiceWithParameters::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        assertNotNull(testObject.fetchService(mockServiceRequest))
    }
}