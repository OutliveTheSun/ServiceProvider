package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.*
import com.outlivethesun.serviceprovider.api.testData.IService
import com.outlivethesun.serviceprovider.api.testData.Service
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
    fun remove(){
        testObject.fetch<IService>()
        testObject.remove(IService::class)
        assertNull(testObject.find<IService>())
    }

    @Test
    fun register(){

    }

    @Test
    fun registerMulti(){

    }
}