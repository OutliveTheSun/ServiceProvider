package com.outlivethesun.implementationtest

import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.fetch
import com.outlivethesun.serviceprovider.api.Unautowirable

interface ITooMany
class TooMany1: ITooMany
class TooMany2: ITooMany

interface IAnnoInterface
@Unautowirable
class AnnoClass : IAnnoInterface

fun main() {
//    unautowirableError()
    tooManyClassesError()
}

fun tooManyClassesError() {
    SP.fetch<ITooMany>()
}

fun unautowirableError(){
    SP.fetch<IAnnoInterface>()
}
