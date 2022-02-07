//package com.outlivethesun.implementationtest

import com.outlivethesun.implementationtest.internal.withinHierarchy.IInternalService
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch

class Test()

fun main() {

    //error due to commented out package in the top
    val service = SP.fetch<IInternalService>()
//    SP.fetch<IService>()

}