package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.api.testData.IServiceAutowire
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("SP")
internal class SPRemoveTest {
    @Test
    fun remove() {
        val service = SP.fetch<IServiceAutowire>()
        SP.remove<IServiceAutowire>()
        Assertions.assertNotEquals(service, SP.fetch<IServiceAutowire>())
    }

    @Test
    fun removeNotInline() {
        val service = SP.fetch<IServiceAutowire>()
        SP.remove(IServiceAutowire::class)
        Assertions.assertNotEquals(service, SP.fetch<IServiceAutowire>())
    }
}