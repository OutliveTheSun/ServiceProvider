//package com.outlivethesun.implementationtest

import com.outlivethesun.implementationtest.internal.withinHierarchy.IInternalService
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch

class Test()

fun main() {

    val service = SP.fetch<IInternalService>()
//    SP.fetch<IService>()

}