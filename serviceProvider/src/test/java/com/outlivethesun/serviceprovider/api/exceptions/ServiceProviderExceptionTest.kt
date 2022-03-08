package com.outlivethesun.serviceprovider.api.exceptions

import com.outlivethesun.serviceprovider.api.exceptions.ServiceProviderException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ServiceProviderExceptionTest{

    @Test
    fun createServiceProviderException(){
        assertNotNull(ServiceProviderException("I am an exception"))
    }
}