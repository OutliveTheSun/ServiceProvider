package com.outlivethesun.implementationtest

import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.annotations.Unautowirable
import com.outlivethesun.serviceprovider.api.fetch

//interface I1
//interface I2 : I1
//
//class C1: I1
//class C2: I2

interface I1
interface I2 : I1
interface I3 : I1

class C1: I1
@Unautowirable
class C2: I2
@Unautowirable
class C3: I3

fun main() {
    SP.fetch<I1>()
}