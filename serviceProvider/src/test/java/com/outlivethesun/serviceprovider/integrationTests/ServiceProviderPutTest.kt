package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.put
import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.Service
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

interface IService3
class Service3 : IService3

internal class ServiceProviderPutTest {
    private val testObject = ServiceProvider()
    
    @Test
    fun put() {
        val service = Service()
        testObject.put<IService>(service)
        Assertions.assertEquals(service, testObject.fetch<IService>())
    }

    @Test
    fun overwriteService() {
        val service = Service()
        val newService = Service()
        testObject.put<IService>(service)
        testObject.put<IService>(newService)
        Assertions.assertEquals(newService, testObject.fetch<IService>())
    }

    @Test
    fun putCached() {
        val service = Service()
        testObject.put<IService>(service)
        Assertions.assertEquals(testObject.fetch<IService>(), testObject.fetch<IService>())
    }

    @Test
    fun putMultipleServices() {
        testObject.put<IService>(Service())
        testObject.put<IService3>(Service3())
    }
}