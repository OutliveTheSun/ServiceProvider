package com.outlivethesun.serviceprovider.integrationTests

import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.remove
import com.outlivethesun.serviceprovider.api.testData.IServiceAutowire
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ServiceProviderRemoveTest {
    private val testObject = ServiceProvider()

    @Test
    fun remove() {
        val service = testObject.fetch<IServiceAutowire>()
        testObject.remove<IServiceAutowire>()
        Assertions.assertNotEquals(service, testObject.fetch<IServiceAutowire>())
    }
}