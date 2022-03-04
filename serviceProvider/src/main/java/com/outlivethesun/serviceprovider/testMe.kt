package com.outlivethesun.serviceprovider

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


class Tree()
class Forest(val tree: Tree)


class CreateWithParameter {
    fun <T: Any> create(kClass: KClass<out T>) : T{
        var parameterInstance: Any? = null
        kClass.primaryConstructor?.parameters?.forEach { parameter ->
            val kClassOfParameter = (parameter.type.classifier) as KClass<Any>
            parameterInstance = kClassOfParameter.primaryConstructor?.call()
        }
        return kClass.primaryConstructor?.call(parameterInstance)!!
    }
}

fun main() {
    val myForest = CreateWithParameter().create(Forest::class)
}