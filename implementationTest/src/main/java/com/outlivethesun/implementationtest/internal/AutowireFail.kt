package com.outlivethesun.implementationtest.internal

import com.outlivethesun.implementationtest.internal.withinHierarchy.IInternalService
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch

fun main() {
    val service = SP.fetch<IInternalService>()
}