package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass

class ServiceDefinitionFactory : IServiceDefinitionFactory {
    override fun <A : Any> createByInstance(instance: A): ServiceDefinition<A> {
        return ServiceDefinition(instance::class, ServiceInstanceType.SINGLE_INSTANCEABLE, instance)
    }

    override fun <A : Any> createByType(
        concreteServiceType: KClass<out Any>,
        serviceInstanceType: ServiceInstanceType
    ): ServiceDefinition<A> {
        return ServiceDefinition(concreteServiceType, serviceInstanceType)
    }
}