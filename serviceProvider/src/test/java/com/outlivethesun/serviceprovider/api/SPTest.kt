package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.internal.ServiceProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SPTest {
    @Test
    fun isNotNull(){
        assertNotNull(SP)
    }

    @Test
    fun isTypeServiceProvider(){
        assertTrue(SP is ServiceProvider)
    }

    @Test
    fun isCached(){
        assertEquals(SP, SP)
    }
}