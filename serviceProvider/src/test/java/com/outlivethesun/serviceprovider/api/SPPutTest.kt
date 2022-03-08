package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.Service
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

interface IService3
class Service3 : IService3

@Tag("SP")
internal class SPPutTest {
    @Test
    fun put() {
        val service = Service()
        SP.put<IService>(service)
        Assertions.assertEquals(service, SP.fetch<IService>())
    }

    @Test
    fun putNotInline() {
        val service = Service()
        SP.put(IService::class, service)
        Assertions.assertEquals(service, SP.fetch<IService>())
    }

    @Test
    fun overwriteService() {
        val service = Service()
        val newService = Service()
        SP.put<IService>(service)
        SP.put<IService>(newService)
        Assertions.assertEquals(newService, SP.fetch<IService>())
    }

    @Test
    fun putCached() {
        val service = Service()
        SP.put<IService>(service)
        Assertions.assertEquals(SP.fetch<IService>(), SP.fetch<IService>())
    }

    @Test
    fun putMultipleServices() {
        SP.put<IService>(Service())
        SP.put<IService3>(Service3())
    }
}