package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import kotlin.reflect.KClass

/**
 * This interface is responsible for creating instances of type [IServiceDefinition]
 */
internal interface IServiceDefinitionFactory {
    fun <T : Any> createByInstance(instance: T): IServiceDefinition<T>

    fun <T : Any> createByType(
        concreteServiceType: KClass<out T>,
        serviceInstanceType: ServiceInstanceType
    ): IServiceDefinition<T>
}