package com.outlivethesun.serviceprovider.api

import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfoException
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinition
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionFactory
import kotlin.reflect.KClass

object SP : IServiceProvider {
    private val serviceProvider = ServiceProviderDefault()

    @Suppress("UNCHECKED_CAST")
    private class ServiceProviderDefault : IServiceProvider {
        private val serviceDefinitionFactory by lazy { ServiceDefinitionFactory() }
        private val serviceDefinitions = mutableMapOf<KClass<*>, ServiceDefinition<*>>()
        private val reflectionsInfo by lazy { ReflectionInfo() }.also { //TODO: add IReflectionInfo to SP
        }

        override fun <A : Any> fetch(abstractServiceType: KClass<A>): A {
            val serviceDefinition =
                serviceDefinitions[abstractServiceType] ?: autowireServiceDefinition(abstractServiceType)
            return serviceDefinition.fetchService() as A
        }

        override fun <A : Any> find(abstractServiceType: KClass<A>): A? {
            return serviceDefinitions[abstractServiceType]?.let { it.fetchService() as A }
        }

        override fun <A : Any> put(abstractServiceType: KClass<A>, service: A) {
            serviceDefinitions[abstractServiceType] =
                serviceDefinitionFactory.createByInstance(abstractServiceType, service)
        }

        override fun <A : Any> remove(abstractServiceType: KClass<A>) {
            serviceDefinitions.remove(abstractServiceType)
        }

        override fun <A : Any> register(
            abstractServiceType: KClass<A>,
            concreteServiceType: KClass<out A>,
            serviceInstanceType: ServiceInstanceType
        ) {
            serviceDefinitions[abstractServiceType] =
                serviceDefinitionFactory.createByType(
                    abstractServiceType,
                    concreteServiceType,
                    serviceInstanceType
                )
        }

        private fun <A : Any> autowireServiceDefinition(abstractServiceType: KClass<A>): ServiceDefinition<A> {
            val listOfKClasses: List<KClass<*>>
            try {
                listOfKClasses = reflectionsInfo.findImplementingClassesOfInterface(abstractServiceType)
            } catch (e: ReflectionInfoException) {
                throw ServiceProviderException(e.message)
            }

            var numberOfKClasses = listOfKClasses.size
            val concreteServiceType = when (listOfKClasses.size) {
                1 -> {
                    val implementingClass = listOfKClasses.first()
                    if (implementingClass.java.isAnnotationPresent(Unautowirable::class.java)) {
                        numberOfKClasses = 0
                        null
                    } else {
                        implementingClass
                    }
                }
                else -> null
            }
                ?: throw RuntimeException("Unable to create service \"${abstractServiceType.simpleName}\". $numberOfKClasses classes found to autowire.")
            val serviceDefinition = serviceDefinitionFactory.createByType(
                abstractServiceType,
                concreteServiceType,
                ServiceInstanceType.SINGLE_INSTANCEABLE
            )
            serviceDefinitions[abstractServiceType] = serviceDefinition
            return serviceDefinition
        }
    }

    override fun <A : Any> fetch(abstractServiceType: KClass<A>): A {
        return serviceProvider.fetch(abstractServiceType)
    }

    override fun <A : Any> find(abstractServiceType: KClass<A>): A? {
        return serviceProvider.find(abstractServiceType)
    }

    override fun <A : Any> put(abstractServiceType: KClass<A>, service: A) {
        serviceProvider.put(abstractServiceType, service)
    }

    override fun <A : Any> remove(abstractServiceType: KClass<A>) {
        serviceProvider.remove(abstractServiceType)
    }

    override fun <A : Any> register(
        abstractServiceType: KClass<A>,
        concreteServiceType: KClass<out A>,
        serviceInstanceType: ServiceInstanceType
    ) {
        serviceProvider.register(abstractServiceType, concreteServiceType, serviceInstanceType)
    }
}