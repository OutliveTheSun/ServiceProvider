package com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker

import com.outlivethesun.serviceprovider.api.IServiceProvider
import kotlin.reflect.KClass

/**
 * An implementation of this interface is responsible for keeping track of a fetched type by the [IServiceProvider].
 * It is used to avoid circular references during the fetching process by keeping track of which type is currently being asked for.
 */
interface ITypeFetchingTracker {
    fun addType(type: KClass<out Any>)
    fun checkTypeIsNotTracked(type: KClass<out Any>)
}