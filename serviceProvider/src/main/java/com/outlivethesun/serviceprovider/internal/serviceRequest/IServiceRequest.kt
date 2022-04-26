package com.outlivethesun.serviceprovider.internal.serviceRequest

import com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker.ITypeFetchingTracker
import kotlin.reflect.KClass

/**
 * An implementation of this interface is responsible for holding all information concerning a unique service request.
 */
interface IServiceRequest {
    val requestedServiceType: KClass<out Any>
    val typeFetchingTracker: ITypeFetchingTracker
}