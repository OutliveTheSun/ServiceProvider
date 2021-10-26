package com.outlivethesun.serviceprovider

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SPTest {
    interface IService
    class Service : IService

    @Test
    fun setService() {
        val service = Service()
        SP.setService(IService::class, service)
        assertEquals(service, SP.getService(IService::class))
    }

    @Test
    fun overwriteService() {
        val service = Service()
        val newService = Service()
        SP.setService(IService::class, service)
        SP.setService(IService::class, newService)
        assertEquals(newService, SP.getService(IService::class))
    }

    @Test
    fun autowire() {
        assertNotNull(SP.getService(SPTest::class))
    }

    @Test
    fun getServiceUncached() {
        assertNotEquals(SP.getService(IService::class), SP.getService(IService::class))
    }
}