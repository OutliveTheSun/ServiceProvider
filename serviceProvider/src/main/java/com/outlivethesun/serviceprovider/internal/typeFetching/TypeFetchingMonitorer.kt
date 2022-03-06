package com.outlivethesun.serviceprovider.internal.typeFetching

import com.outlivethesun.serviceprovider.api.CircularReferenceServiceProviderException
import kotlin.reflect.KClass

class TypeFetchingMonitorer : ITypeFetchingMonitorer {
    private val typesInFetchingProcess = mutableListOf<KClass<out Any>>()

    override fun addAsInFetchingProcess(type: KClass<out Any>) {
        typesInFetchingProcess.add(type)
    }

    override fun checkIfFetchingAllowed(type: KClass<out Any>) {
        if (typesInFetchingProcess.contains(type)) {
            typesInFetchingProcess.remove(type)
            throw CircularReferenceServiceProviderException(type)
        }
    }

    override fun removeFromBeingFetched(type: KClass<out Any>) {
        typesInFetchingProcess.remove(type)
    }
}