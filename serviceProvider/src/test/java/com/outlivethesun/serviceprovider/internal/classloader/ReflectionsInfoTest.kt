package com.outlivethesun.serviceprovider.internal.classloader

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import kotlin.reflect.KClass

internal class ReflectionsInfoTest{

    companion object{
//        const val packagePath = "unsinn"
        const val packagePath = "com.outlivethesun"
    }

    private val resultSet : Set<Class<out IService>> = setOf()
    private val reflectionsInfo = ReflectionsInfo()

    init {
        mockkConstructor(Reflections::class)
//        every { constructedWith<Reflections>(OfTypeMatcher<KClass<IService>>(KClass::class), EqMatcher("packagePath")) }
        every { constructedWith<Reflections>(EqMatcher(packagePath), EqMatcher(Scanners.SubTypes)).getSubTypesOf(IService::class.java) } returns resultSet
    }

    @Test
    fun findImplementingClassesOfInterface(){
        val implementingClasses = reflectionsInfo.findImplementingClassesOfInterface(IService::class, packagePath)
        assertClassFound(implementingClasses)
    }

    @Test
    fun findImplementingClassesOfInterfaceDefaultPath(){
        val implementingClasses = reflectionsInfo.findImplementingClassesOfInterface(IService::class)
        assertClassFound(implementingClasses)
    }

    @Test
    fun findImplementingClassesOfInterfaceDefaultPathCached(){
        reflectionsInfo.findImplementingClassesOfInterface(IService::class)
        val implementingClasses = reflectionsInfo.findImplementingClassesOfInterface(IService::class)
        assertClassFound(implementingClasses)
    }

    @Test
    fun findImplementingClassesOfInterfaceUnmocked(){
        unmockkAll()
        reflectionsInfo.findImplementingClassesOfInterface(IService::class)
        val implementingClasses = reflectionsInfo.findImplementingClassesOfInterface(IService::class)
        assertClassFound(implementingClasses)
    }

    private fun assertClassFound(implementingClasses: List<KClass<*>>) {
        assertEquals(1, implementingClasses.size)
        assertEquals(Service::class, implementingClasses.first())
    }
}