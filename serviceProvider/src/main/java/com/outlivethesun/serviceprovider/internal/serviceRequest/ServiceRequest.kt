package com.outlivethesun.serviceprovider.internal.serviceRequest

import com.outlivethesun.serviceprovider.internal.ServiceProvider
import com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker.ITypeFetchingTracker
import com.outlivethesun.serviceprovider.internal.serviceRequest.typeFetchingTracker.TypeFetchingTracker
import kotlin.reflect.KClass

internal class ServiceRequest(
    override val serviceProvider: ServiceProvider,
    override val requestedServiceType: KClass<out Any>
) : IServiceRequest {
    override val typeFetchingTracker: ITypeFetchingTracker by lazy { TypeFetchingTracker() }
}