package com.outlivethesun.implementationtest

import com.outlivethesun.implementationtest.internal.withinHierarchy.IInternalService
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch
import org.junit.jupiter.api.Test

class TestMe: IInternalService {

    @Test
    fun testIfAutowireFails(){
        val service = SP.fetch<IInternalService>()
    }

}