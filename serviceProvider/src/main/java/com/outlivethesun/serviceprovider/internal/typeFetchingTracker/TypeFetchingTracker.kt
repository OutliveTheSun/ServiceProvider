package com.outlivethesun.serviceprovider.internal.typeFetchingTracker

import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import kotlin.reflect.KClass

class TypeFetchingTracker : ITypeFetchingTracker {
    private val typesInFetchingProcess = mutableListOf<KClass<out Any>>()

    override fun startTracking(type: KClass<out Any>) {
        typesInFetchingProcess.add(type)
    }

    override fun checkIsNotAlreadyTracking(type: KClass<out Any>) {
        if (typesInFetchingProcess.contains(type)) {
            typesInFetchingProcess.remove(type)
            throw CircularReferenceServiceProviderException(type)
        }
    }

    override fun stopTracking(type: KClass<out Any>) {
        typesInFetchingProcess.remove(type)
    }
}