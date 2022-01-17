package com.outlivethesun.serviceprovider.internal.classloader

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import kotlin.reflect.KClass

internal class ReflectionsInfo {

    fun <A : Any> findImplementingClassesOfInterface(
        interfaceName: KClass<A>,
        packagePath: String = ""
    ): List<KClass<out A>> {
        var path = packagePath
        if (path.isEmpty()) {
            val firstStackElementClassname = Thread.currentThread().stackTrace.last().className
            checkIsValidPackageName(firstStackElementClassname)
            path = firstStackElementClassname.substringBefore(".")
        }
        val reflections = Reflections(path, Scanners.SubTypes)
        return reflections.getSubTypesOf(interfaceName.java).map { it.kotlin }
    }

    private fun checkIsValidPackageName(stackElementClassName: String) {
        if (!stackElementClassName.contains(".")) {
            throw RuntimeException("Classloading error: The package cannot be determined. Missing package information in calling program: $stackElementClassName")
        }
    }
}