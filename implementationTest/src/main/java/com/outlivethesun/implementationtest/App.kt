package com.outlivethesun.implementationtest

import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.annotations.Unautowirable
import com.outlivethesun.serviceprovider.api.annotations.MultiInstantiable
import kotlin.reflect.full.hasAnnotation

interface ISomeService
class SomeService : ISomeService
interface IAnotherService
class AnotherService : IAnotherService
interface IRandomService
class RandomService : IRandomService
interface IServiceWithSeveralConstructorParameters
class ServiceWithSeveralConstructorParameters(
    val someService: ISomeService,
    val anotherService: IAnotherService,
    val randomService: IRandomService
) : IServiceWithSeveralConstructorParameters

interface IInstanceParameter
class InstanceParameter : IInstanceParameter

interface IWithInstanceParameter
class WithInstanceParameter(val instanceParameter: IInstanceParameter) : IWithInstanceParameter

interface INoImplementations

interface ITooMany
class TooMany1 : ITooMany
class TooMany2 : ITooMany

interface IAnnoInterface

@Unautowirable
class AnnoClass : IAnnoInterface

interface ICircularRef
class CircularRef(circularRef: ICircularRef) : ICircularRef

interface IMultiInstantiable

@MultiInstantiable
class MultiInstantiableService : IMultiInstantiable

fun main() {
//    unautowirableError()
//    tooManyClassesError()
//    noClassesError()
//    withInstanceParameter()
//    circularReference()
//    severalServices()
//    multiInstantiable()
}

fun multiInstantiable() {
    val isMulti = MultiInstantiableService::class.hasAnnotation<MultiInstantiable>()
}

fun severalServices() {
    val service = SP.fetch<IServiceWithSeveralConstructorParameters>()
}

fun withInstanceParameter() {
    val withInstanceParameter = SP.fetch<IWithInstanceParameter>()
}

fun circularReference() {
    SP.fetch<ICircularRef>()
}

fun noClassesError() {
    SP.fetch<INoImplementations>()
}

fun tooManyClassesError() {
    SP.fetch<ITooMany>()
}

fun unautowirableError() {
    SP.fetch<IAnnoInterface>()
}
