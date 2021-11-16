package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import kotlin.reflect.KClass

internal interface IServiceDefinitionFactory {
    fun <A : Any> createByInstance(
        abstractServiceType: KClass<*>,
        instance: A): ServiceDefinition<A>
    fun <A : Any> createByType(
        abstractServiceType: KClass<*>,
        concreteServiceType: KClass<out Any>,
        serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANCEABLE
    ): ServiceDefinition<A>
}