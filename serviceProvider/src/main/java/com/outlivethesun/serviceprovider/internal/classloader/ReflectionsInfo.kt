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
            val firstStackElement = Thread.currentThread().stackTrace.last()
            checkIsValidPackageName(interfaceName, firstStackElement)
            path = firstStackElement.className.substringBefore(".")
        }
        val reflections = Reflections(path, Scanners.SubTypes)
        return reflections.getSubTypesOf(interfaceName.java).map { it.kotlin }
    }

    private fun <A : Any> checkIsValidPackageName(interfaceName: KClass<A>, stackElement: StackTraceElement) {
        if (!stackElement.className.contains(".")) {
            throw ReflectionInfoMissingPackageException(interfaceName.simpleName!!, stackElement.fileName)
        }
    }
}