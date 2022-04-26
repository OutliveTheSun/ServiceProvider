package com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker

import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("TypeFetchingTracker")
internal class TypeFetchingTrackerTest {

    private val kClass = TypeFetchingTrackerTest::class
    private val testObject: ITypeFetchingTracker = TypeFetchingTracker()

    @Test
    fun checkTypeIsAlreadyTrackedExc() {
        testObject.addType(kClass)
        try {
            testObject.checkTypeIsNotTracked(kClass)
            fail("Exception expected")
        } catch (e: CircularReferenceServiceProviderException) {
            assertEquals(kClass, e.abstractTypeToAutowire)
        }
    }

    @Test
    fun checkTypeIsNotTracked() {
        testObject.addType(kClass)
        try {
            testObject.checkTypeIsNotTracked(TypeFetchingTracker::class)
        } catch (e: CircularReferenceServiceProviderException) {
            fail()
        }
    }

    @Test
    fun checkTypeIsNotTrackedInit() {
        try {
            testObject.checkTypeIsNotTracked(kClass)
        } catch (e: CircularReferenceServiceProviderException) {
            fail()
        }
    }

    @Test
    fun addTypeMultipleTimes() {
        testObject.addType(kClass)
        testObject.addType(kClass)
    }
}