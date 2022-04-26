package com.outlivethesun.serviceprovider.internal.serviceDefinition

import com.outlivethesun.reflectioninfo.IReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfoException
import com.outlivethesun.serviceprovider.api.annotations.Unautowirable
import com.outlivethesun.serviceprovider.api.exceptions.*
import com.outlivethesun.serviceprovider.internal.extensions.getServiceInstanceType
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

internal class ServiceDefinitionDictionary(
    private val serviceDefinitionFactory: IServiceDefinitionFactory = ServiceDefinitionFactory(),
    private val reflectionInfo: IReflectionInfo
) : IServiceDefinitionDictionary {
    override fun <T : Any> fetch(abstractServiceType: KClass<T>): IServiceDefinition<T> {
        val concreteServiceType = if (abstractServiceType.java.isInterface) {
            findConcreteServiceTypeByInterface(abstractServiceType)
        } else {
            findConcreteServiceTypeByClass(abstractServiceType)
        }
        return serviceDefinitionFactory.createByType(
            concreteServiceType,
            concreteServiceType.getServiceInstanceType()
        )
    }

    private fun <T : Any> findConcreteServiceTypeByClass(abstractServiceType: KClass<T>) =
        if (abstractServiceType.hasAnnotation<Unautowirable>()) {
            throw NoClassFoundUnautowireableAnnotationPresentException(abstractServiceType, abstractServiceType)
        } else {
            abstractServiceType
        }

    private fun <T : Any> findConcreteServiceTypeByInterface(
        abstractServiceType: KClass<T>
    ): KClass<out T> {
        val (validImplementingClasses, unautowirableImplementingClasses) = findImplementingClassesSplitByAnnotationUnautowirable(
            abstractServiceType
        )
        return when (validImplementingClasses.size) {
            1 -> validImplementingClasses.first()
            else -> raiseException(abstractServiceType, validImplementingClasses, unautowirableImplementingClasses)
        }
    }

    private fun <T : Any> raiseException(
        abstractServiceType: KClass<out T>,
        validImplementingClasses: List<KClass<out T>>,
        unautowirableImplementingClasses: List<KClass<out T>>
    ): Nothing {
        if (validImplementingClasses.isEmpty()) {
            handleNoImplementingClasses(unautowirableImplementingClasses, abstractServiceType)
        } else if (validImplementingClasses.size > 1) {
            handleTooManyImplementingClasses(abstractServiceType, validImplementingClasses)
        } else {
            throw ServiceProviderException("Technical exception")
        }
    }

    private fun <T : Any> handleTooManyImplementingClasses(
        abstractServiceType: KClass<out T>,
        validImplementingClasses: List<KClass<out T>>
    ): Nothing {
        throw TooManyClassesFoundServiceProviderException(abstractServiceType, validImplementingClasses.size)
    }

    private fun <T : Any> handleNoImplementingClasses(
        unautowirableImplementingClasses: List<KClass<out T>>,
        abstractServiceType: KClass<out T>
    ): Nothing {
        if (unautowirableImplementingClasses.isEmpty()) {
            throw NoClassFoundAutowireException(abstractServiceType)
        } else {
            throw NoClassFoundUnautowireableAnnotationPresentException(
                abstractServiceType,
                unautowirableImplementingClasses.first()
            )
        }
    }

    /**
     * Returns a pair with:
     *
     * - first: list containing implementing classes of [abstractServiceType] not having annotation [Unautowirable]
     * - second: list containing implementing classes of [abstractServiceType] having annotation [Unautowirable]
     */
    private fun <T : Any> findImplementingClassesSplitByAnnotationUnautowirable(abstractServiceType: KClass<T>): Pair<List<KClass<out T>>, List<KClass<out T>>> {
        try {
            return reflectionInfo.findImplementingClassesOfInterface(abstractServiceType)
                .partition { implementingClass ->
                    !implementingClass.hasAnnotation<Unautowirable>()
                }
        } catch (e: ReflectionInfoException) {
            throw ServiceDefinitionDictionaryException(
                "Technical error when trying to find implementing classes of '$abstractServiceType'. Check the origin exception '${e::class.simpleName}'",
                e
            )
        }
    }
}