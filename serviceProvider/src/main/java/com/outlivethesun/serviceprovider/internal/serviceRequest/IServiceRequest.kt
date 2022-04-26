package com.outlivethesun.serviceprovider.internal.serviceRequest

import com.outlivethesun.serviceprovider.internal.ServiceProvider
import com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker.ITypeFetchingTracker
import kotlin.reflect.KClass

/**
 * An implementation of this interface is responsible for holding all information concerning a unique service request.
 */
internal interface IServiceRequest {
    val serviceProvider: ServiceProvider
    val requestedServiceType: KClass<out Any>
    val typeFetchingTracker: ITypeFetchingTracker
}