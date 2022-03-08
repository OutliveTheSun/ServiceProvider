package com.outlivethesun.serviceprovider.api.exceptions

import kotlin.reflect.KClass

open class AutowireServiceProviderException(abstractTypeToAutowire: KClass<out Any>, additionalMessage: String? = null) :
    ServiceProviderException("Unable to create instance for service '${abstractTypeToAutowire.simpleName}'.${additionalMessage?.let{" $it"}?:""}")