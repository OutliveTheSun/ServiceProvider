package com.outlivethesun.serviceprovider.internal.classloader

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.Service
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ReflectionsInfoTest{

    val reflectionsInfo = ReflectionsInfo()

    @Test
    fun findImplementingClassesOfInterface(){
        val implementingClasses = reflectionsInfo.findImplementingClassesOfInterface(IService::class)
        assertEquals(1, implementingClasses.size)
        assertEquals(Service::class, implementingClasses.first())
    }
}