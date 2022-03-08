package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.exceptions.InvalidConstructorParameterTypeServiceProviderException
import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import java.sql.Timestamp
import java.time.LocalDate
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
        return if (serviceInstanceType == ServiceInstanceType.SINGLE_INSTANTIABLE) {
            if (singleInstance == null) {
                singleInstance = createInstance()
            }
            singleInstance!!
        } else {
            createInstance()
        }
    }

    private fun createInstance(): A {
        concreteServiceType.primaryConstructor!!.parameters.apply {
            return when (this.size) {
                0 -> concreteServiceType.createInstance()
                else -> createWithParameters(concreteServiceType, this)
            }
        }
    }

    private fun createWithParameters(concreteServiceType: KClass<out A>, parameters: List<KParameter>): A {
        val parameterValues = mutableListOf<Any?>()
        parameters.forEach { parameter ->
            val parameterType = parameter.type.classifier as KClass<Any>
            val parameterValue = if (isBasicType(parameterType)) {
                throw InvalidConstructorParameterTypeServiceProviderException(concreteServiceType, parameterType)
            } else {
                SP.fetch(parameterType)
            }
            parameterValues.add(parameterValue)
        }
        return concreteServiceType.primaryConstructor!!.call(*parameterValues.toTypedArray())
    }

    private fun isBasicType(parameterType: KClass<Any>): Boolean {
        return when (parameterType) {
            String::class, Char::class, Byte::class, Short::class, Int::class, Boolean::class, Long::class,
            Float::class, Double::class, Timestamp::class, LocalDate::class -> true
            else -> false
        }
    }
}