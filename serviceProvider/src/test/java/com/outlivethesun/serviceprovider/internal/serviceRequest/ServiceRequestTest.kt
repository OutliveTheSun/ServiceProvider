package com.outlivethesun.serviceprovider.internal.serviceRequest

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class ServiceRequestTest {

    private val abstractServiceType = ServiceRequestTest::class
    private val testObject = ServiceRequest(abstractServiceType)

    @Test
    fun getAbstractServiceType() {
        assertEquals(abstractServiceType, testObject.requestedServiceType)
    }

    @Test
    fun typeFetchingTrackerBound() {
        assertNotNull(testObject.typeFetchingTracker)
    }

    @Test
    fun typeFetchingTrackerUnique() {
        assertEquals(testObject.typeFetchingTracker, testObject.typeFetchingTracker)
    }
}