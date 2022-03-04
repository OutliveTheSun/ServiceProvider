package com.outlivethesun.serviceprovider.api

import com.outlivethesun.reflectioninfo.IReflectionInfo
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

        //boot load ReflectionInfo
        private val reflectionInfo by lazy { ReflectionInfo() }.also { reflectionInfoLazy ->
            put<IReflectionInfo>(reflectionInfoLazy.value)
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
            checkServiceDefinitionsForExistingEntry(abstractServiceType)
            val concreteServiceType: KClass<out A>
            if (abstractServiceType.java.isInterface) {
                val listOfKClasses: List<KClass<*>>
                try {
                    listOfKClasses = reflectionInfo.findImplementingClassesOfInterface(abstractServiceType)
                } catch (e: ReflectionInfoException) {
                    throw ServiceProviderException(e.message)
                }

                val numberOfKClasses = listOfKClasses.size
                concreteServiceType = when (listOfKClasses.size) {
                    1 -> {
                        val implementingClass = listOfKClasses.first()
                        if (implementingClass.java.isAnnotationPresent(Unautowirable::class.java)) {
                            throw UnableToCreateServiceException(
                                abstractServiceType,
                                "0 classes found to autowire. Possible solution: Class '${implementingClass.simpleName}' found but it is annotated with '@${Unautowirable::class.simpleName}'. Remove annotation to use service '${implementingClass.simpleName}'."
                            )
                        } else {
                            implementingClass
                        }
                    }
                    else -> null
                }
                    ?: throw UnableToCreateServiceException(
                        abstractServiceType,
                        "$numberOfKClasses classes found to autowire."
                    )
            } else {
                concreteServiceType = abstractServiceType
            }
            val serviceDefinition = serviceDefinitionFactory.createByType(
                abstractServiceType,
                concreteServiceType,
                ServiceInstanceType.SINGLE_INSTANCEABLE
            )
            serviceDefinitions[abstractServiceType] = serviceDefinition
            return serviceDefinition
        }

        /**
         * Check if a service was already requested to avoid endless loop for circular reference
         */
        fun <A : Any> checkServiceDefinitionsForExistingEntry(abstractServiceType: KClass<A>) {
            if(serviceDefinitions[abstractServiceType] != null){
                throw UnableToCreateServiceException(abstractServiceType, "Circular reference occurred when trying to autowire the service. Consider registering the service '${abstractServiceType.simpleName}' manually.")
            }
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