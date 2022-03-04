package com.outlivethesun.serviceprovider

import com.outlivethesun.serviceprovider.api.SP
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

interface IRoad
class Road : IRoad
interface ITree
class Tree : ITree
class Forest(val tree: ITree, val road: IRoad)

class CreateWithParameter {
    fun <T : Any> create(concreteServiceType: KClass<out T>): T {
        val parameterInstances = mutableListOf<Any>()
        concreteServiceType.primaryConstructor?.parameters?.forEach { parameter ->
            val parameterType = (parameter.type.classifier) as KClass<Any>
            parameterInstances.add(SP.fetch(parameterType))
        }
        return concreteServiceType.primaryConstructor?.call(*parameterInstances.toTypedArray())!!
    }

    fun testVarargsInt(vararg numbers: Int) {
        numbers.forEach { println(it) }
    }

    fun testVarargsObjects(vararg objects: Any) {
        objects.forEach { println(it) }
    }
}

fun main() {
    val myForest = CreateWithParameter().create(Forest::class)

//    val objectArray = arrayOf(Tree(), Tree())
//    val listToArray = listOf<Any>(Tree(), Road()).toTypedArray()
//    CreateWithParameter().testVarargsObjects(*listToArray)
//
//    val array = arrayOf(1,2,3,4).toIntArray()
//    CreateWithParameter().testVarargsInt(*array)
}
