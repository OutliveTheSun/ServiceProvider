package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.register
import com.outlivethesun.serviceprovider.api.testData.IServiceWithTwoImplementations
import com.outlivethesun.serviceprovider.api.testData.Service1
import com.outlivethesun.serviceprovider.api.testData.Service2
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ServiceProviderRegisterTest {
    private val testObject = ServiceProvider()

    @Test
    fun register() {
        testObject.register<IServiceWithTwoImplementations, Service1>()
        val fetchedService = testObject.fetch<IServiceWithTwoImplementations>()
        Assertions.assertNotNull(fetchedService)
        Assertions.assertEquals(Service1::class, fetchedService::class)
    }

    @Test
    fun registerUncached() {
        testObject.register<IServiceWithTwoImplementations, Service1>()
        Assertions.assertNotEquals(
            testObject.fetch<IServiceWithTwoImplementations>(),
            testObject.fetch<IServiceWithTwoImplementations>()
        )
    }

    @Test
    fun registerOverwrite() {
        testObject.register<IServiceWithTwoImplementations, Service1>()
        testObject.register<IServiceWithTwoImplementations, Service2>()
        Assertions.assertEquals(Service2::class, testObject.fetch<IServiceWithTwoImplementations>()::class)
    }

    @Test
    fun registerWithSingleInstantiableType() {
        testObject.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANTIABLE)
        Assertions.assertNotNull(testObject.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerWithSingleInstantiableCached() {
        testObject.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANTIABLE)
        Assertions.assertEquals(
            testObject.fetch<IServiceWithTwoImplementations>(),
            testObject.fetch<IServiceWithTwoImplementations>()
        )
    }
}