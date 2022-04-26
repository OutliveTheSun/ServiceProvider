package com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker

import com.outlivethesun.serviceprovider.api.exceptions.CircularReferenceServiceProviderException
import kotlin.reflect.KClass

class TypeFetchingTracker : ITypeFetchingTracker {
    private val typesInFetchingProcess = hashSetOf<KClass<out Any>>()

    override fun addType(type: KClass<out Any>) {
        typesInFetchingProcess.add(type)
    }

    override fun checkTypeIsNotTracked(type: KClass<out Any>) {
        if (typesInFetchingProcess.contains(type)) {
            throw CircularReferenceServiceProviderException(type)
        }
    }
}