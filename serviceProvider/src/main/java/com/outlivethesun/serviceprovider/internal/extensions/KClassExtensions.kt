package com.outlivethesun.serviceprovider.internal.extensions

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.annotations.MultiInstantiable
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

internal fun KClass<out Any>.getServiceInstanceType(): ServiceInstanceType {
    return if (this.hasAnnotation<MultiInstantiable>()) {
        ServiceInstanceType.MULTI_INSTANTIABLE
    } else {
        ServiceInstanceType.SINGLE_INSTANTIABLE
    }
}