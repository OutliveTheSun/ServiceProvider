package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass

interface IServiceDefinitionFactory {
    fun <A : Any> createByInstance(instance: A): ServiceDefinition<A>
    fun <A : Any> createByType(
        concreteServiceType: KClass<out Any>,
        serviceInstanceType: ServiceInstanceType = ServiceInstanceType.MULTI_INSTANCEABLE
    ): ServiceDefinition<A>
}