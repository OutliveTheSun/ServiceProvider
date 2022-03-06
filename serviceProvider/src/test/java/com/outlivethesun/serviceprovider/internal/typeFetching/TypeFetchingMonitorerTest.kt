package com.outlivethesun.serviceprovider.internal.typeFetching

import com.outlivethesun.serviceprovider.IService
import com.outlivethesun.serviceprovider.api.CircularReferenceServiceProviderException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

internal class TypeFetchingMonitorerTest {

    private val testObject: ITypeFetchingMonitorer = TypeFetchingMonitorer()

    @Test
    fun addAsInFetchingProcess() {
        testObject.addAsInFetchingProcess(IService::class)
        try {
            testObject.checkIfFetchingAllowed(IService::class)
            fail("Expected to be in process")
        }catch (e: CircularReferenceServiceProviderException){}
    }

    @Test
    fun checkIfFetchingAllowedTrue() {
        testObject.checkIfFetchingAllowed(IService::class)
    }

    @Test
    fun removeFromBeingFetched() {
        testObject.addAsInFetchingProcess(IService::class)
        testObject.removeFromBeingFetched(IService::class)
        testObject.checkIfFetchingAllowed(IService::class)
    }
}