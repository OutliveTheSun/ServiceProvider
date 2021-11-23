package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import kotlin.reflect.KClass

internal class ServiceDefinitionFactory : IServiceDefinitionFactory {
    override fun <A : Any> createByInstance(abstractServiceType: KClass<A>, instance: A): ServiceDefinition<A> {
        return ServiceDefinition(
            abstractServiceType,
            instance::class,
            ServiceInstanceType.SINGLE_INSTANCEABLE,
            instance
        )
    }

    override fun <A : Any> createByType(
        abstractServiceType: KClass<A>,
        concreteServiceType: KClass<out A>,
        serviceInstanceType: ServiceInstanceType
    ): ServiceDefinition<A> {
        return ServiceDefinition(abstractServiceType, concreteServiceType, serviceInstanceType)
    }
}