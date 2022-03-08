package com.outlivethesun.serviceprovider.api.exceptions

import kotlin.reflect.KClass

class InvalidConstructorParameterTypeServiceProviderException(
    classTypeToInstantiate: KClass<out Any>,
    invalidParameterType: KClass<out Any>
) : ServiceProviderException("Unable to create service \"${classTypeToInstantiate.simpleName}\". The constructor of class \"${classTypeToInstantiate.simpleName}\" contains invalid parameter type '${invalidParameterType.simpleName}'. Ensure it only contains instantiable parameters.")