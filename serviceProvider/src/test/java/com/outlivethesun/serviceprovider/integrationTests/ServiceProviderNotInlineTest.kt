package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.*
import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.IServiceWithTwoImplementations
import com.outlivethesun.serviceprovider.api.testData.Service
import com.outlivethesun.serviceprovider.api.testData.Service1
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ServiceProviderNotInlineTest {

    private val testObject = ServiceProvider()

    @Test
    fun fetch() {
        assertEquals(testObject.fetch(IService::class), testObject.fetch<IService>())
    }

    @Test
    fun fetchOrNull() {
        assertEquals(testObject.fetchOrNull(IService::class), testObject.fetchOrNull<IService>())
    }

    @Test
    fun find() {
        assertEquals(testObject.find(IService::class), testObject.find<IService>())
    }

    @Test
    fun put() {
        val service = Service()
        testObject.put(service)
        assertEquals(service, testObject.find<Service>())
    }

    @Test
    fun remove() {
        testObject.fetch<IService>()
        testObject.remove(IService::class)
        assertNull(testObject.find<IService>())
    }

    @Test
    fun registerInit() {
        testObject.register(IServiceWithTwoImplementations::class, Service1::class)
        assertTrue(testObject.find<IServiceWithTwoImplementations>() is Service1)
    }

    @Test
    fun registerMultiInstantiable() {
        testObject.register(IServiceWithTwoImplementations::class, Service1::class)
        assertNotEquals(
            testObject.find<IServiceWithTwoImplementations>(),
            testObject.find<IServiceWithTwoImplementations>()
        )
    }

    @Test
    fun registerSingleInstantiable() {
        testObject.register(
            IServiceWithTwoImplementations::class,
            Service1::class,
            ServiceInstanceType.SINGLE_INSTANTIABLE
        )
        assertEquals(
            testObject.find<IServiceWithTwoImplementations>(),
            testObject.find<IServiceWithTwoImplementations>()
        )
    }
}