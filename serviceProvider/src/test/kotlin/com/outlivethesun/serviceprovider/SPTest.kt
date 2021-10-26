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
    fun setService() {
        val service = Service()
        SP.setService(IService::class, service)
        assertEquals(service, SP.getService<IService>())
    }

    @Test
    fun overwriteService() {
        val service = Service()
        val newService = Service()
        SP.setService(IService::class, service)
        SP.setService(IService::class, newService)
        assertEquals(newService, SP.getService<IService>())
    }

    @Test
    fun autowire() {
        assertNotNull(SP.getService<IServiceAutowire>())
    }

    @Test
    fun getServiceUncached() {
        assertNotEquals(SP.getService<IServiceAutowire>(), SP.getService<IServiceAutowire>())
    }

    @Test
    fun getServiceUnautowirable() {
        try {
            SP.getService<IServiceWithTwoImplementations>()
            fail()
        } catch (e: RuntimeException) { }
    }

    @Test
    fun setServiceCheckCached() {
        val service = Service()
        SP.setService(IService::class, service)
        assertEquals(SP.getService<IService>(), SP.getService<IService>())
    }
}