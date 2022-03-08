package com.outlivethesun.serviceprovider.internal.typeFetchingTracker

import kotlin.reflect.KClass

interface ITypeFetchingTracker {
    fun startTracking(type: KClass<out Any>)
    fun stopTracking(type: KClass<out Any>)
    fun checkIsNotAlreadyTracking(type: KClass<out Any>)
}