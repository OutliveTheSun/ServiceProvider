package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

internal data class ServiceDefinition<A : Any>(
    private val abstractServiceType: KClass<A>,
    private val concreteServiceType: KClass<out A>,
    private val serviceInstanceType: ServiceInstanceType,
    private var singleInstance: A? = null
) {

    fun fetchService(): A {
        return if (serviceInstanceType == ServiceInstanceType.SINGLE_INSTANCEABLE) {
            if (singleInstance == null) {
                singleInstance = createInstance()
            }
            singleInstance!!
        } else {
            createInstance()
        }
    }

    private fun createInstance(): A {
//        try {
//            return concreteServiceType.createInstance()
//        } catch (e: IllegalArgumentException) {
//            throw RuntimeException("Unable to create service \"${abstractServiceType.simpleName}\". The constructor of class \"${concreteServiceType.simpleName}\" must not have any parameters.")
//        }
//    }
        concreteServiceType.primaryConstructor!!.parameters.apply {
            return when (this.size) {
                0 -> concreteServiceType.createInstance()
                else -> createWithParameters(concreteServiceType, this)
            }
        }
    }

    private fun createWithParameters(concreteServiceType: KClass<out A>, parameters: List<KParameter>): A {
        val parameterInstances = mutableListOf<Any>()
        parameters.forEach { parameter ->
            val parameterType = (parameter.type.classifier) as KClass<Any>
            parameterInstances.add(SP.fetch(parameterType))
        }
        return concreteServiceType.primaryConstructor!!.call(*parameterInstances.toTypedArray())
    }
}