package com.outlivethesun.serviceprovider.api

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

interface IServiceForFindNotToBeFound

interface IServiceForFind
class ServiceForFind : IServiceForFind

@Tag("SP")
internal class SPFindTest {
    @Test
    fun find() {
        SP.register<IServiceForFind, ServiceForFind>()
        Assertions.assertNotNull(SP.find<IServiceForFind>())
    }

    @Test
    fun findNull() {
        Assertions.assertNull(SP.find<IServiceForFindNotToBeFound>())
    }

    @Test
    fun findNullNotInline() {
        Assertions.assertNull(SP.find(IServiceForFindNotToBeFound::class))
    }
}