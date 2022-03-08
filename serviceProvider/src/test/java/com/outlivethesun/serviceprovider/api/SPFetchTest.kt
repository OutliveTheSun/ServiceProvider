package com.outlivethesun.serviceprovider.api

import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import com.outlivethesun.serviceprovider.api.exceptions.NoClassFoundServiceProviderException
import com.outlivethesun.serviceprovider.api.exceptions.UnautowirableServiceProviderException
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinition
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionDictionary
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionFactory
import com.outlivethesun.serviceprovider.internal.typeFetchingTracker.TypeFetchingTracker
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

class FetchService
class FetchCachedService
class FetchOrNullService
class CircularReferenceService
class UnautowirableExcService
class FetchOrNullNoClassFoundExcService
class TrackerService

@Tag("SP")
internal class SPFetchTest {
    /**
     * In order for 'anyConstructed' to work for every method, the mocked objects have to be set up once at the beginning.
     * When 'anyConstructed' is called inside a test and the constructed object (which happens once statically in the SP)
     * was already instantiated within another method, the behaviour cannot be changed anymore.
     */
    companion object {
        private lateinit var mockFetchService: FetchService
        private lateinit var mockFetchOrNullService: FetchOrNullService

        @JvmStatic
        @BeforeAll
        fun setup() {
            mockkConstructor(ReflectionInfo::class)
            mockkConstructor(ServiceDefinitionFactory::class)
            mockkConstructor(ServiceDefinitionDictionary::class)
            mockkConstructor(TypeFetchingTracker::class)
            justRun { anyConstructed<TypeFetchingTracker>().checkIsNotAlreadyTracking(any()) }
            justRun { anyConstructed<TypeFetchingTracker>().startTracking(any()) }
            justRun { anyConstructed<TypeFetchingTracker>().stopTracking(any()) }
            every { anyConstructed<TypeFetchingTracker>().checkIsNotAlreadyTracking(CircularReferenceService::class) } throws CircularReferenceServiceProviderException(
                CircularReferenceService::class
            )
//            justRun { anyConstructed<TypeFetchingTracker>().checkIsNotAlreadyTracking(TrackerService::class) }
            mockFetchService = setupServiceDefinition(FetchService::class)
            mockFetchOrNullService = setupServiceDefinition(FetchOrNullService::class)
            setupServiceDefinition(FetchCachedService::class)
            every { anyConstructed<ServiceDefinitionDictionary>().fetch(UnautowirableExcService::class) } throws UnautowirableServiceProviderException(
                UnautowirableExcService::class,
                UnautowirableExcService::class
            )
            every { anyConstructed<ServiceDefinitionDictionary>().fetch(FetchOrNullNoClassFoundExcService::class) } throws NoClassFoundServiceProviderException(
                FetchOrNullNoClassFoundExcService::class
            )
        }

        private inline fun <reified T : Any> setupServiceDefinition(type: KClass<T>): T {
            val mockServiceDefinition = mockk<ServiceDefinition<T>>()
            val mockService = mockk<T>()
            every { anyConstructed<ServiceDefinitionDictionary>().fetch(type) } returns mockServiceDefinition
            every { mockServiceDefinition.fetchService() } returns mockService
            every {
                anyConstructed<ServiceDefinitionFactory>().createByType(
                    type,
                    type,
                    ServiceInstanceType.SINGLE_INSTANTIABLE
                )
            } returns mockServiceDefinition
            return mockService
        }
    }

    @Test
    fun fetch() {
        assertEquals(mockFetchService, SP.fetch<FetchService>())
    }

    @Test
    fun fetchCached(){
        assertEquals(SP.fetch(FetchCachedService::class), SP.fetch(FetchCachedService::class))
    }

    @Test
    fun fetchOrNull() {
        assertEquals(mockFetchOrNullService, SP.fetchOrNull<FetchOrNullService>())
    }

    @Test
    fun fetchOrNullUnautowirableExc() {
        assertNull(SP.fetchOrNull<UnautowirableExcService>())
    }

    @Test
    fun fetchOrNullNoClassFoundExc() {
        assertNull(SP.fetchOrNull<FetchOrNullNoClassFoundExcService>())
    }

    @Test
    fun fetchAlreadyTrackedCircularReferenceExc() {
        try {
            SP.fetch<CircularReferenceService>()
            fail("Exception not raised")
        } catch (e: CircularReferenceServiceProviderException) {
        }
    }

    @Test
    fun checkTracker() {
        SP.fetch<TrackerService>()
        verify { anyConstructed<TypeFetchingTracker>().checkIsNotAlreadyTracking(TrackerService::class) }
        verify { anyConstructed<TypeFetchingTracker>().startTracking(any()) }
        verify { anyConstructed<TypeFetchingTracker>().stopTracking(any()) }
    }
}