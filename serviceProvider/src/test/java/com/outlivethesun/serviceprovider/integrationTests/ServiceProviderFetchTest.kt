package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.fetchOrNull
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

interface IFetchService
class FetchService : IFetchService
interface IFetchNoImplementationService

internal class ServiceProviderFetchTest {
    private val testObject = ServiceProvider()

    @Test
    fun fetchClass() {
        assertNotNull(testObject.fetch<FetchService>())
    }

    @Test
    fun fetchInterface() {
        assertNotNull(testObject.fetch<IFetchService>())
    }

    @Test
    fun fetchInterfaceCached() {
        assertEquals(testObject.fetch(IFetchService::class), testObject.fetch(IFetchService::class))
    }

    @Test
    fun fetchOrNullExisting() {
        assertNotNull(testObject.fetchOrNull<IFetchService>())
    }

    @Test
    fun fetchOrNullNotExisting() {
        assertNull(testObject.fetchOrNull<IFetchNoImplementationService>())
    }


//    @Test
//    fun checkTracker() {
//        testObject.fetch<TrackerService>()
//        verify { anyConstructed<TypeFetchingTracker>().checkTypeIsNotTracked(TrackerService::class) }
//        verify { anyConstructed<TypeFetchingTracker>().addType(any()) }
//    }
}