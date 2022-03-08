package com.outlivethesun.serviceprovider.api.exceptions

import com.outlivethesun.serviceprovider.api.Unautowirable
import kotlin.reflect.KClass

class TooManyClassesFoundServiceProviderException(abstractTypeToAutowire: KClass<out Any>, numberOfFoundClasses: Int) :
    AutowireServiceProviderException(
        abstractTypeToAutowire,
        "There are $numberOfFoundClasses classes implementing the interface '${abstractTypeToAutowire.simpleName}'. Consider using annotation '@${Unautowirable::class.simpleName}' to reduce the number of implementing classes to one."
    )