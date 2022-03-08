package com.outlivethesun.serviceprovider.internal.typeFetchingTracker

import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("TypeFetchingTracker")
internal class TypeFetchingTrackerTest {

    private val kClass = TypeFetchingTrackerTest::class
    private val testObject: ITypeFetchingTracker = TypeFetchingTracker()

    @Test
    fun startTracking() {
        testObject.startTracking(kClass)
        try{
            testObject.checkIsNotAlreadyTracking(kClass)
            fail("Exception expected")
        }catch (e: CircularReferenceServiceProviderException){
        }
    }

    @Test
    fun checkIsNotTracking() {
        try{
            testObject.checkIsNotAlreadyTracking(kClass)
        }catch (e: CircularReferenceServiceProviderException){
            fail("Expected to not be thrown")
        }
    }

    @Test
    fun checkTrackingIsStoppedAfterException(){
        testObject.startTracking(kClass)
        try{
            testObject.checkIsNotAlreadyTracking(kClass)
            fail("unexpected")
        }catch (e: CircularReferenceServiceProviderException){
        }
        try{
            testObject.checkIsNotAlreadyTracking(kClass)
        }catch (e: CircularReferenceServiceProviderException){
            fail("Expected to be removed from tracking")
        }
    }

    @Test
    fun stopTracking() {
        testObject.startTracking(kClass)
        testObject.stopTracking(kClass)
        try{
            testObject.checkIsNotAlreadyTracking(kClass)
        }catch (e: CircularReferenceServiceProviderException){
            fail("Expected to be stopped")
        }
    }
}