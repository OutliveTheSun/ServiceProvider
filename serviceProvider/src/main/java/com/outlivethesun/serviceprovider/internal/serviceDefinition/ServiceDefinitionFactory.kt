package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.internal.extensions.getServiceInstanceType
import kotlin.reflect.KClass

internal class ServiceDefinitionFactory : IServiceDefinitionFactory {
    override fun <T : Any> createByInstance(instance: T): IServiceDefinition<T> {
        return ServiceDefinition(
            instance::class,
            instance::class.getServiceInstanceType(),
            instance
        )
    }

    override fun <T : Any> createByType(
        concreteServiceType: KClass<out T>,
        serviceInstanceType: ServiceInstanceType
    ): IServiceDefinition<T> {
        return ServiceDefinition(concreteServiceType, serviceInstanceType)
    }
}