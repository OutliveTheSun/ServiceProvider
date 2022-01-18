package com.outlivethesun.serviceprovider.internal.classloader

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import kotlin.reflect.KClass

class ReflectionsInfo {

    private lateinit var defaultReflections: Reflections

    fun <A : Any> findImplementingClassesOfInterface(
        interfaceName: KClass<A>,
        packagePath: String = ""
    ): List<KClass<out A>> {
        val reflections = if (packagePath.isEmpty()) {
            fetchDefaultReflections(interfaceName)
        } else {
            createReflections(packagePath)
        }
        return reflections.getSubTypesOf(interfaceName.java).map { it.kotlin }
    }

    private fun <A : Any> fetchDefaultReflections(interfaceName: KClass<A>): Reflections {
        return if (!this::defaultReflections.isInitialized) {
            createReflections(determinePathFromStack(interfaceName)).apply { defaultReflections = this }
        } else {
            defaultReflections
        }
    }

    private fun createReflections(path: String): Reflections {
        return Reflections(path, Scanners.SubTypes)
    }

    private fun <A : Any> checkIsValidPackageName(interfaceName: KClass<A>, stackElement: StackTraceElement) {
        if (!stackElement.className.contains(".")) {
            throw ReflectionInfoMissingPackageException(interfaceName.simpleName!!, stackElement.fileName)
        }
    }

    private fun <A : Any> determinePathFromStack(interfaceName: KClass<A>): String {
        val firstStackElement = Thread.currentThread().stackTrace.last()
        checkIsValidPackageName(interfaceName, firstStackElement)
        return firstStackElement.className.substringBefore(".")
    }
}