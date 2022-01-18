package com.outlivethesun.serviceprovider.internal.classloader

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ReflectionInfoExceptionTest {

    companion object {
        private const val interfaceName = "Interface"
        private val runningProgramName = "RunningProgramName"
    }

    @Test
    fun createReflectionInfoMissingPackageException() {
        val exception = ReflectionInfoMissingPackageException(interfaceName, runningProgramName)
        assertTrue(exception.message!!.contains(interfaceName))
        assertTrue(exception.message!!.contains(runningProgramName))
    }
}