package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SPTest {

    interface IServiceWithTwoImplementations
    class Service1 : IServiceWithTwoImplementations
    class Service2 : IServiceWithTwoImplementations

    interface IServiceAutowire
    class ServiceAutowire : IServiceAutowire

    interface IServiceUnautowireable

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
}