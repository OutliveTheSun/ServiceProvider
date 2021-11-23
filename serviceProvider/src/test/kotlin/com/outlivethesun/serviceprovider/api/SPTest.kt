package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

internal class SPTest {

    interface IServiceWithTwoImplementations
    class Service1 : IServiceWithTwoImplementations
    class Service2 : IServiceWithTwoImplementations

    interface IServiceAutowire
    class ServiceAutowire : IServiceAutowire

    interface IServiceUnautowireable

    interface IService3
    class Service3 : IService3

    @Unautowirable
    class ServiceUnautowireable : IServiceUnautowireable

    @Test
    fun put() {
        val service = Service()
        SP.put<IService>(service)
        assertEquals(service, SP.fetch<IService>())
    }

    @Test
    fun putNotInline() {
        val service = Service()
        SP.put(IService::class, service)
        assertEquals(service, SP.fetch<IService>())
    }

    @Test
    fun overwriteService() {
        val service = Service()
        val newService = Service()
        SP.put<IService>(service)
        SP.put<IService>(newService)
        assertEquals(newService, SP.fetch<IService>())
    }

    @Test
    fun autowire() {
        assertNotNull(SP.fetch<IServiceAutowire>())
    }

    @Test
    fun fetchUncached() {
        assertEquals(SP.fetch<IServiceAutowire>(), SP.fetch<IServiceAutowire>())
    }

    @Test
    fun fetchUnautowirableTwoImplementations() {
        try {
            SP.fetch<IServiceWithTwoImplementations>()
            fail()
        } catch (e: RuntimeException) {
        }
    }

    @Test
    fun fetchUnautowirable() {
        try {
            SP.fetch<IServiceUnautowireable>()
            fail()
        } catch (e: RuntimeException) {
        }
    }

    @Test
    fun putCached() {
        val service = Service()
        SP.put<IService>(service)
        assertEquals(SP.fetch<IService>(), SP.fetch<IService>())
    }

    @Test
    fun remove() {
        val service = SP.fetch<IServiceAutowire>()
        SP.remove<IServiceAutowire>()
        assertNotEquals(service, SP.fetch<IServiceAutowire>())
    }

    @Test
    fun removeNotInline() {
        val service = SP.fetch<IServiceAutowire>()
        SP.remove(IServiceAutowire::class)
        assertNotEquals(service, SP.fetch<IServiceAutowire>())
    }

    @Test
    fun fetchNotInline() {
        val service = Service()
        SP.put<IService>(service)
        assertEquals(service, SP.fetch(IService::class))
    }

    @Test
    fun putMultipleServices() {
        SP.put<IService>(Service())
        SP.put<IService3>(Service3())
    }

    @Test
    fun register() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        val fetchedService = SP.fetch<IServiceWithTwoImplementations>()
        assertNotNull(fetchedService)
        assertEquals(Service1::class, fetchedService::class)
    }

    @Test
    fun registerNotInline() {
        SP.register(IServiceWithTwoImplementations::class, Service1::class)
        assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerUncached() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        assertNotEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerWithSingleInstanceableTypeNotInline() {
        SP.register(IServiceWithTwoImplementations::class, Service1::class, ServiceInstanceType.SINGLE_INSTANCEABLE)
        assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
        assertEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerOverwrite() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        SP.register<IServiceWithTwoImplementations, Service2>()
        assertEquals(Service2::class, SP.fetch<IServiceWithTwoImplementations>()::class)
    }

    @Test
    fun registerWithSingleInstanceableType() {
        SP.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANCEABLE)
        assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerWithSingleInstanceableCached() {
        SP.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANCEABLE)
        assertEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }
}