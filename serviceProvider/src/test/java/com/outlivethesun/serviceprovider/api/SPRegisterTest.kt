package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.api.testData.IServiceWithTwoImplementations
import com.outlivethesun.serviceprovider.api.testData.Service1
import com.outlivethesun.serviceprovider.api.testData.Service2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("SP")
class SPRegisterTest {
    @Test
    fun register() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        val fetchedService = SP.fetch<IServiceWithTwoImplementations>()
        Assertions.assertNotNull(fetchedService)
        Assertions.assertEquals(Service1::class, fetchedService::class)
    }

    @Test
    fun registerNotInline() {
        SP.register(IServiceWithTwoImplementations::class, Service1::class)
        Assertions.assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerUncached() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        Assertions.assertNotEquals(
            SP.fetch<IServiceWithTwoImplementations>(),
            SP.fetch<IServiceWithTwoImplementations>()
        )
    }

    @Test
    fun registerWithSingleInstantiableTypeNotInline() {
        SP.register(IServiceWithTwoImplementations::class, Service1::class, ServiceInstanceType.SINGLE_INSTANTIABLE)
        Assertions.assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
        Assertions.assertEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerOverwrite() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        SP.register<IServiceWithTwoImplementations, Service2>()
        Assertions.assertEquals(Service2::class, SP.fetch<IServiceWithTwoImplementations>()::class)
    }

    @Test
    fun registerWithSingleInstantiableType() {
        SP.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANTIABLE)
        Assertions.assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerWithSingleInstanceableCached() {
        SP.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANTIABLE)
        Assertions.assertEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }
}