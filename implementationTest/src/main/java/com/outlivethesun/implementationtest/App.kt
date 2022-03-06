package com.outlivethesun.implementationtest

import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.Unautowirable

interface IInstanceParameter
class InstanceParameter: IInstanceParameter

interface IWithInstanceParameter
class WithInstanceParameter(val instanceParameter: IInstanceParameter): IWithInstanceParameter

//interface ICircularReference
//class Circular1(): ICircularReference


interface INoImplementations

interface ITooMany
class TooMany1: ITooMany
class TooMany2: ITooMany

interface IAnnoInterface
@Unautowirable
class AnnoClass : IAnnoInterface

interface ICircularRef
class CircularRef(circularRef: ICircularRef): ICircularRef

fun main() {
//    unautowirableError()
//    tooManyClassesError()
//    noClassesError()
//    withInstanceParameter()
    circularReference()
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

fun unautowirableError(){
    SP.fetch<IAnnoInterface>()
}
