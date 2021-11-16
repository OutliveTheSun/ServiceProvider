package com.outlivethesun.serviceprovider.internal.classloader

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import kotlin.reflect.KClass

internal class ReflectionsInfo {

    fun <A : Any> findImplementingClassesOfInterface(
        interfaceName: KClass<A>,
        packagePath: String = interfaceName.java.packageName
    ): List<KClass<out A>> {
        val reflections = Reflections(packagePath, SubTypesScanner())
        return reflections.getSubTypesOf(interfaceName.java).map { it.kotlin }
    }
}