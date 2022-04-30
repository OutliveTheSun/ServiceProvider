package com.outlivethesun.serviceprovider.api

import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import com.outlivethesun.serviceprovider.api.exceptions.NoClassFoundAutowireException
import com.outlivethesun.serviceprovider.api.testData.*
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import com.outlivethesun.serviceprovider.internal.serviceDefinition.IServiceDefinition
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionDictionary
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionFactory
import com.outlivethesun.serviceprovider.internal.serviceRequest.ServiceRequest
import com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker.TypeFetchingTracker
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ServiceProviderTest {
    private val mockServiceDefinition = mockk<IServiceDefinition<IService>>()
    private val mockServiceDefinitionForService1 = mockk<IServiceDefinition<Service1>>()
    private val mockService = mockk<IService>()
    private val abstractServiceType = IService::class
    private val testObject = ServiceProvider()

    init {
        mockkConstructor(ReflectionInfo::class)
        mockkConstructor(ServiceDefinitionFactory::class)
        mockkConstructor(ServiceDefinitionDictionary::class)
        mockkConstructor(TypeFetchingTracker::class)
        mockkConstructor(ServiceRequest::class)

        justRun { anyConstructed<TypeFetchingTracker>().checkTypeIsNotTracked(abstractServiceType) }
        every { anyConstructed<ServiceDefinitionDictionary>().fetch(abstractServiceType) } returns mockServiceDefinition
        every { mockServiceDefinition.fetchService(any()) } returns mockService
    }

    @Test
    fun fetchInit() {
        assertEquals(mockService, testObject.fetch<IService>())
    }

    @Test
    fun fetchCheckTrackerCalled() {
        testObject.fetch<IService>()
        verify { anyConstructed<TypeFetchingTracker>().addType(abstractServiceType) }
    }

    @Test
    fun fetchCircularReferenceExc() {
        every { anyConstructed<TypeFetchingTracker>().checkTypeIsNotTracked(abstractServiceType) } throws (CircularReferenceServiceProviderException(
            abstractServiceType
        ))
        try {
            testObject.fetch<IService>()
            fail()
        } catch (e: CircularReferenceServiceProviderException) {
        }
    }

    @Test
    fun fetchCached() {
        assertEquals(testObject.fetch<IService>(), testObject.fetch<IService>())
        verify(exactly = 1) { anyConstructed<ServiceDefinitionDictionary>().fetch(abstractServiceType) }
    }

    @Test
    fun fetchOrNullCreate() {
        assertEquals(mockService, testObject.fetchOrNull<IService>())
    }

    @Test
    fun fetchOrNullFetchExisting() {
        assertEquals(testObject.fetch<IService>(), testObject.fetchOrNull<IService>())
    }

    @Test
    fun fetchOrNullNoClassFoundExc() {
        every { anyConstructed<ServiceDefinitionDictionary>().fetch(abstractServiceType) } throws NoClassFoundAutowireException(
            abstractServiceType
        )
        assertNull(testObject.fetchOrNull<IService>())
    }

    @Test
    fun findExisting() {
        assertEquals(testObject.fetch<IService>(), testObject.find<IService>())
    }

    @Test
    fun findNotExisting() {
        assertNull(testObject.find<IService>())
    }

    @Test
    fun findCheckTrackerCalled() {
        testObject.fetch<IService>()
        testObject.find<IService>()
        verify { anyConstructed<TypeFetchingTracker>().addType(abstractServiceType) }
    }

    @Test
    fun put() {
        val service = Service()
        testObject.put<IService>(service)
        assertEquals(service, testObject.fetch<IService>())
    }

    @Test
    fun putOverride() {
        val service2 = Service2()
        testObject.put<IServiceWithTwoImplementations>(Service1())
        testObject.put<IServiceWithTwoImplementations>(service2)
        assertEquals(service2, testObject.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun removeInit(){
        testObject.remove<IService>()
        assertNull(testObject.find<IService>())
    }

    @Test
    fun remove(){
        testObject.fetch<IService>()
        testObject.remove<IService>()
        assertNull(testObject.find<IService>())
    }

    @Test
    fun register(){
        val service = Service1()
        every { anyConstructed<ServiceDefinitionFactory>().createByType(Service1::class, ServiceInstanceType.MULTI_INSTANTIABLE) } returns mockServiceDefinitionForService1
        every { mockServiceDefinitionForService1.fetchService(any()) } returns service
        testObject.register(IServiceWithTwoImplementations::class, Service1::class)
        assertEquals(service, testObject.find<IServiceWithTwoImplementations>())
        verify { anyConstructed<ServiceDefinitionFactory>().createByType(Service1::class, ServiceInstanceType.MULTI_INSTANTIABLE) }
    }

    @Test
    fun registerSingleInstantiable(){
        every { anyConstructed<ServiceDefinitionFactory>().createByType(Service1::class, ServiceInstanceType.SINGLE_INSTANTIABLE) } returns mockServiceDefinitionForService1
        testObject.register(IServiceWithTwoImplementations::class, Service1::class, ServiceInstanceType.SINGLE_INSTANTIABLE)
        verify { anyConstructed<ServiceDefinitionFactory>().createByType(Service1::class, ServiceInstanceType.SINGLE_INSTANTIABLE) }
    }

    @Test
    fun registerMultiInstantiable(){
        every { anyConstructed<ServiceDefinitionFactory>().createByType(Service1::class, ServiceInstanceType.MULTI_INSTANTIABLE) } returns mockServiceDefinitionForService1
        testObject.register(IServiceWithTwoImplementations::class, Service1::class, ServiceInstanceType.MULTI_INSTANTIABLE)
        verify { anyConstructed<ServiceDefinitionFactory>().createByType(Service1::class, ServiceInstanceType.MULTI_INSTANTIABLE) }
    }
}