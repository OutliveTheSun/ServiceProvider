package com.outlivethesun.serviceprovider.internal.typeFetching

import kotlin.reflect.KClass

interface ITypeFetchingMonitorer {
    fun addAsInFetchingProcess(type: KClass<out Any>)
    fun removeFromBeingFetched(type: KClass<out Any>)
    fun checkIfFetchingAllowed(type: KClass<out Any>)
}