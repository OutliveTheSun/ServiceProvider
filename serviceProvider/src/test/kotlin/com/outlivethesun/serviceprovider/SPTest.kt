package com.outlivethesun.serviceprovider

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SPTest {
    interface IService
    class Service : IService

    interface IServiceWithTwoImplementations
    class Service1 : IServiceWithTwoImplementations
    class Service2 : IServiceWithTwoImplementations

    interface IServiceAutowire
    class ServiceAutowire : IServiceAutowire

    @Test
    fun put() {
        val service = Service()
        SP.put(IService::class, service)
        assertEquals(service, SP.fetch<IService>())
    }

    @Test
    fun overwriteService() {
        val service = Service()
        val newService = Service()
        SP.put(IService::class, service)
        SP.put(IService::class, newService)
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
    fun fetchUnautowirable() {
        try {
            SP.fetch<IServiceWithTwoImplementations>()
            fail()
        } catch (e: RuntimeException) {
        }
    }

    @Test
    fun putCheckCached() {
        val service = Service()
        SP.put(IService::class, service)
        assertEquals(SP.fetch<IService>(), SP.fetch<IService>())
    }
}