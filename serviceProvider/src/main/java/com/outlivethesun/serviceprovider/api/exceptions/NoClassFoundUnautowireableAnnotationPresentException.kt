package com.outlivethesun.serviceprovider.api.exceptions

import com.outlivethesun.serviceprovider.api.annotations.Unautowirable
import kotlin.reflect.KClass

class NoClassFoundUnautowireableAnnotationPresentException(
    abstractTypeToAutowire: KClass<out Any>,
    implementingClassAnnotatedWithUnautowirable: KClass<out Any>
) : NoClassFoundAutowireException(
    abstractTypeToAutowire,
    "Possible solution: Class '${implementingClassAnnotatedWithUnautowirable.simpleName}' found but it is annotated with '@${Unautowirable::class.simpleName}'. Remove annotation to use service '${implementingClassAnnotatedWithUnautowirable.simpleName}'."
)