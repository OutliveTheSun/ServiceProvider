package com.outlivethesun.serviceprovider.api

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ServiceProviderExceptionTest{

    @Test
    fun createServiceProviderException(){
        assertNotNull(ServiceProviderException("I am an exception"))
    }
}