package com.outlivethesun.serviceprovider.api

import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfoMissingPackageException
import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import com.outlivethesun.serviceprovider.api.annotations.MultiInstantiable
import io.mockk.every
import io.mockk.mockkConstructor
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.fail

internal class SPTest {

    interface IMultiInstantiableAnnotated
    @MultiInstantiable
    class MultiInstantiableAnnotated: IMultiInstantiableAnnotated

    interface IServiceContainingCircularReference
    class ServiceContainingCircularReference(service: IServiceWithCircularReference) :
        IServiceContainingCircularReference

    interface IServiceWithCircularReference
    class ServiceWithCircularReference(service: IServiceContainingCircularReference) : IServiceWithCircularReference

    interface IServiceContainingItself
    class ServiceContainingItself(service: IServiceContainingItself) : IServiceContainingItself

    interface IServiceWithTwoImplementations
    class Service1 : IServiceWithTwoImplementations
    class Service2 : IServiceWithTwoImplementations

    interface IServiceAutowire
    class ServiceAutowire : IServiceAutowire

    interface IServiceUnautowirable

    interface IService3
    class Service3 : IService3

    interface IService4
    class Service4 : IService4

    interface IServiceForFindNotToBeFound

    interface IServiceForFind
    class ServiceForFind : IServiceForFind

    class ServiceClassToAutowire

    @Unautowirable
    class ServiceUnautowirable : IServiceUnautowirable

    init {
        mockkConstructor(ReflectionInfo::class)
        every {
            anyConstructed<ReflectionInfo>().findImplementingClassesOfInterface(
                IService4::class,
                ""
            )
        } throws (ReflectionInfoMissingPackageException("", ""))
    }

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
            assertNotNull(e)
        }
    }

    @Test
    fun fetchUnautowirable() {
        try {
            SP.fetch<IServiceUnautowirable>()
            fail()
        } catch (e: RuntimeException) {
            assertNotNull(e)
        }
    }

    @Test
    fun fetchOrNullFetch() {
        assertNotNull(SP.fetchOrNull<IServiceForFind>())
    }

    @Test
    fun fetchOrNullFetchNoServiceFoundExc() {
        assertNull(SP.fetchOrNull<IServiceForFindNotToBeFound>())
    }

    @Test
    fun fetchOrNullFetchUnautowirableExc() {
        assertNull(SP.fetchOrNull<IServiceUnautowirable>())
    }

    @Test
    fun find() {
        SP.register<IServiceForFind, ServiceForFind>()
        assertNotNull(SP.find<IServiceForFind>())
    }

    @Test
    fun findNull() {
        assertNull(SP.find<IServiceForFindNotToBeFound>())
    }

    @Test
    fun findNullNotInline() {
        assertNull(SP.find(IServiceForFindNotToBeFound::class))
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

    @Test
    fun fetchOrNullNotInline() {
        assertNotNull(SP.fetchOrNull(IServiceForFind::class))
    }

    @Test
    fun putMultipleServices() {
        SP.put<IService>(Service())
        SP.put<IService3>(Service3())
    }

    @Test
    fun register() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        val fetchedService = SP.fetch<IServiceWithTwoImplementations>()
        assertNotNull(fetchedService)
        assertEquals(Service1::class, fetchedService::class)
    }

    @Test
    fun registerNotInline() {
        SP.register(IServiceWithTwoImplementations::class, Service1::class)
        assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerUncached() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        assertNotEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerWithSingleInstanceableTypeNotInline() {
        SP.register(IServiceWithTwoImplementations::class, Service1::class, ServiceInstanceType.SINGLE_INSTANTIABLE)
        assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
        assertEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerOverwrite() {
        SP.register<IServiceWithTwoImplementations, Service1>()
        SP.register<IServiceWithTwoImplementations, Service2>()
        assertEquals(Service2::class, SP.fetch<IServiceWithTwoImplementations>()::class)
    }

    @Test
    fun registerWithSingleInstanceableType() {
        SP.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANTIABLE)
        assertNotNull(SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun registerWithSingleInstanceableCached() {
        SP.register<IServiceWithTwoImplementations, Service1>(ServiceInstanceType.SINGLE_INSTANTIABLE)
        assertEquals(SP.fetch<IServiceWithTwoImplementations>(), SP.fetch<IServiceWithTwoImplementations>())
    }

    @Test
    fun throwsReflectionsInfoException() {
        try {
            SP.fetch<IService4>()
            fail("Expected ReflectionsInfoException was not thrown")
        } catch (e: ServiceProviderException) {
            assertNotNull(e)
        }
    }

    @Test
    fun autowireServiceClass() {
        assertNotNull(SP.fetch<ServiceClassToAutowire>())
    }

    @Test
    fun autowireSingleInstantiableService() {
        assertEquals(
            SP.fetch<IService>(),
            SP.fetch<IService>()
        )
    }

    @Test
    fun autowireMultiInstantiableService() {
        assertNotEquals(
            SP.fetch<IMultiInstantiableAnnotated>(),
            SP.fetch<IMultiInstantiableAnnotated>()
        )
    }

    @Test
    fun createServiceCircularReferenceExc() {
        try {
            SP.fetch<IServiceWithCircularReference>()
            fail("Expected to prevent circular reference")
        } catch (e: CircularReferenceServiceProviderException) {
        }
    }

    @Test
    fun createServiceWithServiceContainingItselfAsParameterExc() {
        try {
            SP.fetch<IServiceContainingItself>()
            fail("Expected to prevent circular reference")
        } catch (e: CircularReferenceServiceProviderException) {
        }
    }
}