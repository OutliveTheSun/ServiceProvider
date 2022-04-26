package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.serviceprovider.api.SP
import com.outlivethesun.serviceprovider.api.ServiceInstanceType
import com.outlivethesun.serviceprovider.api.exceptions.InvalidConstructorParameterTypeServiceProviderException
import com.outlivethesun.serviceprovider.internal.ServiceProvider
import com.outlivethesun.serviceprovider.internal.serviceRequest.IServiceRequest
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

internal class ServiceDefinition<T : Any>(
    override val concreteServiceType: KClass<out T>,
    override val serviceInstanceType: ServiceInstanceType,
    private var singleInstance: T? = null
) : IServiceDefinition<T> {

    private val unsupportedTypes: HashSet<KClass<out Any>> by lazy { buildUnsupportedTypes() }

    override fun fetchService(serviceRequest: IServiceRequest): T {
        return if (serviceInstanceType == ServiceInstanceType.SINGLE_INSTANTIABLE) {
            if (singleInstance == null) {
                singleInstance = createInstance(serviceRequest)
            }
            singleInstance!!
        } else {
            createInstance(serviceRequest)
        }
    }

    private fun createInstance(serviceRequest: IServiceRequest): T {
        concreteServiceType.primaryConstructor!!.parameters.apply {
            return when (this.size) {
                0 -> concreteServiceType.createInstance()
                else -> createInstanceWithParameters(concreteServiceType, this, serviceRequest)
            }
        }
    }

    private fun createInstanceWithParameters(
        concreteServiceType: KClass<out T>,
        parameters: List<KParameter>,
        serviceRequest: IServiceRequest
    ): T {
        val parameterValues = mutableListOf<Any?>()
        parameters.forEach { parameter ->
            @Suppress("UNCHECKED_CAST")
            val parameterType = parameter.type.classifier as KClass<out Any>
            val parameterValue = if (isBasicType(parameterType)) {
                throw InvalidConstructorParameterTypeServiceProviderException(concreteServiceType, parameterType)
            } else {
                serviceRequest.serviceProvider.fetch(parameterType, serviceRequest)
            }
            parameterValues.add(parameterValue)
        }
        return concreteServiceType.primaryConstructor!!.call(*parameterValues.toTypedArray())
    }

    private fun isBasicType(parameterType: KClass<out Any>): Boolean {
        return unsupportedTypes.contains(parameterType)
    }

    private fun buildUnsupportedTypes(): HashSet<KClass<out Any>> {
        return hashSetOf(
            Boolean::class,
            CharSequence::class,
            Char::class,
            String::class,
            Number::class,
            Byte::class,
            Short::class,
            Int::class,
            Long::class,
            Float::class,
            Double::class,
            Timestamp::class,
            LocalDate::class,
            LocalTime::class
        )
    }

}