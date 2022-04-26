package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.internal.serviceRequest.IServiceRequest
import kotlin.reflect.KClass

/**
 * An implementation of this interface is responsible for managing exactly one service type. This means to either
 * return a single instance for [ServiceInstanceType.SINGLE_INSTANTIABLE] or create multiple instances when the
 * service instance type is [ServiceInstanceType.MULTI_INSTANTIABLE]. The created instances are of type [concreteServiceType]
 * and are fetched by their abstract service type [T].
 */
internal interface IServiceDefinition<T : Any> {
    val concreteServiceType: KClass<out T>
    val serviceInstanceType: ServiceInstanceType
    fun fetchService(serviceRequest: IServiceRequest): T
}