package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.find
import com.outlivethesun.serviceprovider.api.register
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

interface IServiceForFindNotToBeFound

interface IServiceForFind
class ServiceForFind : IServiceForFind

internal class ServiceProviderFindTest {
    private val testObject = ServiceProvider()
    
    @Test
    fun find() {
        testObject.register<IServiceForFind, ServiceForFind>()
        Assertions.assertNotNull(testObject.find<IServiceForFind>())
    }

    @Test
    fun findNull() {
        Assertions.assertNull(testObject.find<IServiceForFindNotToBeFound>())
    }

    @Test
    fun findNullNotInline() {
        Assertions.assertNull(testObject.find(IServiceForFindNotToBeFound::class))
    }
}