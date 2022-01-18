package com.outlivethesun.serviceprovider.internal.classloader

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import kotlin.reflect.KClass

internal class ReflectionsInfoTest{

    companion object{
        const val packagePath = "unsinn"
//        const val packagePath = "com.outlivethesun"
    }

    private val resultSet : MutableSet<Class<out IService>> = mutableSetOf(Service::class.java)
    private val testObject = ReflectionsInfo()

    init {
        mockkConstructor(Reflections::class)
//        every { constructedWith<Reflections>(EqMatcher(packagePath), EqMatcher(Scanners.SubTypes)).getSubTypesOf(IService::class.java) } returns resultSet
//        every { constructedWith<Reflections>(OfTypeMatcher<KClass<IService>>(KClass::class), EqMatcher("packagePath")) }
    }

    @Test
    fun findImplementingClassesOfInterface(){
//        every { anyConstructed<Reflections>().getSubTypesOf(IService::class.java) } returns resultSet
        val implementingClasses = testObject.findImplementingClassesOfInterface(IService::class, packagePath)
        assertClassFound(implementingClasses)
    }

    @Test
    fun findImplementingClassesOfInterfaceDefaultPath(){
        val implementingClasses = testObject.findImplementingClassesOfInterface(IService::class)
        assertClassFound(implementingClasses)
    }

    @Test
    fun findImplementingClassesOfInterfaceDefaultPathCached(){
        testObject.findImplementingClassesOfInterface(IService::class)
        val implementingClasses = testObject.findImplementingClassesOfInterface(IService::class)
        assertClassFound(implementingClasses)
    }

    @Test
    fun findImplementingClassesOfInterfaceUnmocked(){
        unmockkAll()
        testObject.findImplementingClassesOfInterface(IService::class)
        val implementingClasses = testObject.findImplementingClassesOfInterface(IService::class)
        assertClassFound(implementingClasses)
    }

    private fun assertClassFound(implementingClasses: List<KClass<*>>) {
        assertEquals(1, implementingClasses.size)
        assertEquals(Service::class, implementingClasses.first())
    }
}