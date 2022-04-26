package com.outlivethesun.serviceprovider.api.exceptions

import com.outlivethesun.serviceprovider.api.annotations.Unautowirable
import kotlin.reflect.KClass

class UnautowirableServiceProviderException(
    abstractTypeToAutowire: KClass<out Any>,
    implementingClassAnnotatedWithUnautowirable: KClass<out Any>
) : AutowireServiceProviderException(
    abstractTypeToAutowire,
    "No classes found to autowire. Possible solution: Class '${implementingClassAnnotatedWithUnautowirable.simpleName}' found but it is annotated with '@${Unautowirable::class.simpleName}'. Remove annotation to use service '${implementingClassAnnotatedWithUnautowirable.simpleName}'."
)