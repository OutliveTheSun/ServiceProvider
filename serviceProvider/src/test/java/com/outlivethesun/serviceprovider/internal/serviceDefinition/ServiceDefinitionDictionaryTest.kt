package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfoException
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.annotations.Unautowirable
import com.outlivethesun.serviceprovider.api.exceptions.NoClassFoundAutowireException
import com.outlivethesun.serviceprovider.api.exceptions.NoClassFoundUnautowireableAnnotationPresentException
import com.outlivethesun.serviceprovider.api.exceptions.TooManyClassesFoundServiceProviderException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

interface IService
class Service : IService

@Unautowirable
class UnautowirableService : IService

class ServiceClassToFetch

internal class ServiceDefinitionDictionaryTest {
    private val mockServiceDefinitionFactory = mockk<IServiceDefinitionFactory>(relaxed = true)
    private val mockReflectionInfo = mockk<ReflectionInfo>()
    private val testObject = ServiceDefinitionDictionary(mockServiceDefinitionFactory, mockReflectionInfo)


    @Test
    fun fetchClass() {
        val kClass = ServiceClassToFetch::class
        val mockServiceDefinition = mockk<ServiceDefinition<ServiceClassToFetch>>()
        every {
            mockServiceDefinitionFactory.createByType(
                kClass,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        } returns mockServiceDefinition
        assertEquals(mockServiceDefinition, testObject.fetch(ServiceClassToFetch::class))
    }

    @Test
    fun fetchUnautowirableClassExc() {
        try {
            testObject.fetch(UnautowirableService::class)
            fail("Exception expected")
        } catch (e: NoClassFoundUnautowireableAnnotationPresentException) {
        }
    }

    @Test
    fun fetchSingle() {
        val foundClasses = listOf(Service::class)
        every { mockReflectionInfo.findImplementingClassesOfInterface(IService::class) } returns foundClasses
        testObject.fetch(IService::class)
        verify {
            mockServiceDefinitionFactory.createByType(
                Service::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        }
    }

    @Test
    fun fetchSingleReflectionInfoExc() {
        val mockReflectionInfoException = mockk<ReflectionInfoException>(relaxed = true)
        every { mockReflectionInfo.findImplementingClassesOfInterface(IService::class) } throws mockReflectionInfoException
        try {
            testObject.fetch(IService::class)
            fail()
        } catch (e: ServiceDefinitionDictionaryException) {
            assertEquals(mockReflectionInfoException, e.cause)
        }
    }

    @Test
    fun fetchNoClassExc() {
        val foundClasses = listOf<KClass<IService>>()
        every { mockReflectionInfo.findImplementingClassesOfInterface(IService::class) } returns foundClasses
        try {
            testObject.fetch(IService::class)
            fail("Exception not raised")
        } catch (e: NoClassFoundAutowireException) {
        }
    }

    @Test
    fun fetchTooManyClassesExc() {
        val foundClasses = listOf(Service::class, Service::class)
        every { mockReflectionInfo.findImplementingClassesOfInterface(IService::class) } returns foundClasses
        try {
            testObject.fetch(IService::class)
            fail("Exception not raised")
        } catch (e: TooManyClassesFoundServiceProviderException) {
        }
    }

    @Test
    fun fetchOnlyUnautowirableServicesExc() {
        val foundClasses = listOf(UnautowirableService::class, UnautowirableService::class)
        every { mockReflectionInfo.findImplementingClassesOfInterface(IService::class) } returns foundClasses

        try {
            testObject.fetch(IService::class)
            fail("Exception not raised")
        } catch (e: NoClassFoundUnautowireableAnnotationPresentException) {
        }
    }

    @Test
    fun fetchUnautowirableAndTooManyServicesExc() {
        val foundClasses = listOf(Service::class, Service::class, UnautowirableService::class)
        every { mockReflectionInfo.findImplementingClassesOfInterface(IService::class) } returns foundClasses

        try {
            testObject.fetch(IService::class)
            fail("Exception not raised")
        } catch (e: TooManyClassesFoundServiceProviderException) {
        }
    }

    @Test
    fun fetchWithOneUnautowirable() {
        val foundClasses = listOf(UnautowirableService::class, Service::class)
        every { mockReflectionInfo.findImplementingClassesOfInterface(IService::class) } returns foundClasses

        testObject.fetch(IService::class)

        verify {
            mockServiceDefinitionFactory.createByType(
                Service::class,
                ServiceInstanceType.SINGLE_INSTANTIABLE
            )
        }
    }
}