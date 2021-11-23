package com.outlivethesun.serviceprovider.internal.classloader

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.scanners.SubTypesScanner
import kotlin.reflect.KClass

internal class ReflectionsInfo {

    fun <A : Any> findImplementingClassesOfInterface(
        interfaceName: KClass<A>,
        packagePath: String = ""
    ): List<KClass<out A>> {
        var path = packagePath
        if(path.isEmpty()){
            path = interfaceName.java.`package`.name
        }
        val reflections = Reflections(path, Scanners.SubTypes)
        return reflections.getSubTypesOf(interfaceName.java).map { it.kotlin }
    }
}