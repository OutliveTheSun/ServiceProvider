package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfoException
import com.outlivethesun.serviceprovider.api.*
import com.outlivethesun.serviceprovider.api.exceptions.*
import com.outlivethesun.serviceprovider.internal.extensions.getServiceInstanceType
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

internal class ServiceDefinitionDictionary(
    private val serviceDefinitionFactory: IServiceDefinitionFactory = ServiceDefinitionFactory(),
    private val reflectionInfo: ReflectionInfo
) : IServiceDefinitionDictionary {
    override fun <A : Any> fetch(abstractServiceType: KClass<A>): ServiceDefinition<A> {
        val concreteServiceType = if (abstractServiceType.java.isInterface) {
            val (validImplementingClasses, unautowirableImplementingClasses) = findImplementingClassesSplitByAnnotation(
                abstractServiceType
            )
            when (validImplementingClasses.size) {
                1 -> validImplementingClasses.first()
                else -> raiseException(abstractServiceType, validImplementingClasses, unautowirableImplementingClasses)
            }
        } else {
            //class requested as service
            if (abstractServiceType.hasAnnotation<Unautowirable>()) {
                throw UnautowirableServiceProviderException(abstractServiceType, abstractServiceType)
            } else {
                abstractServiceType
            }
        }
        return serviceDefinitionFactory.createByType(
            abstractServiceType,
            concreteServiceType,
            concreteServiceType.getServiceInstanceType()
        )
    }

    private fun <A : Any> raiseException(
        abstractServiceType: KClass<out A>,
        validImplementingClasses: List<KClass<out A>>,
        unautowirableImplementingClasses: List<KClass<out A>>
    ): Nothing {

        if (validImplementingClasses.isEmpty()) {
            if (unautowirableImplementingClasses.isEmpty()) {
                throw NoClassFoundServiceProviderException(abstractServiceType)
            } else {
                throw UnautowirableServiceProviderException(
                    abstractServiceType,
                    unautowirableImplementingClasses.first()
                )
            }
        } else {
            if (unautowirableImplementingClasses.isEmpty()) {
                throw TooManyClassesFoundServiceProviderException(abstractServiceType, validImplementingClasses.size)
            } else {
                throw UnautowirableServiceProviderException(
                    abstractServiceType,
                    unautowirableImplementingClasses.first()
                )
            }
        }
    }

    private fun <A : Any> findImplementingClassesSplitByAnnotation(abstractServiceType: KClass<A>): Pair<List<KClass<out A>>, List<KClass<out A>>> {
        try {
            return reflectionInfo.findImplementingClassesOfInterface(abstractServiceType)
                .partition { implementingClass ->
                    !implementingClass.hasAnnotation<Unautowirable>()
                }
        } catch (e: ReflectionInfoException) {
            throw ServiceProviderException(e.message)
        }
    }
}