package com.outlivethesun.serviceprovider.api

import com.outlivethesun.reflectioninfo.IReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfoException
import com.outlivethesun.serviceprovider.api.annotations.MultiInstantiable
import com.outlivethesun.serviceprovider.internal.getServiceInstanceType
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinition
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionFactory
import com.outlivethesun.serviceprovider.internal.typeFetching.ITypeFetchingMonitorer
import com.outlivethesun.serviceprovider.internal.typeFetching.TypeFetchingMonitorer
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object SP : IServiceProvider {
    private val serviceProvider = ServiceProviderDefault()

    @Suppress("UNCHECKED_CAST")
    private class ServiceProviderDefault : IServiceProvider {
        private val serviceDefinitionFactory by lazy { ServiceDefinitionFactory() }
        private val serviceDefinitions = mutableMapOf<KClass<*>, ServiceDefinition<*>>()
        private val typeFetchingMonitorer: ITypeFetchingMonitorer by lazy { TypeFetchingMonitorer() }

        //boot load ReflectionInfo
        private val reflectionInfo by lazy { ReflectionInfo() }.also { reflectionInfoLazy ->
            put<IReflectionInfo>(reflectionInfoLazy.value)
        }

        override fun <A : Any> fetch(abstractServiceType: KClass<A>): A {
            /**
             * Check if a service was already requested to avoid endless loop for circular reference
             */
            typeFetchingMonitorer.checkIfFetchingAllowed(abstractServiceType)
            val service: A
            try {
                typeFetchingMonitorer.addAsInFetchingProcess(abstractServiceType)
                val serviceDefinition =
                    serviceDefinitions[abstractServiceType] ?: autowireServiceDefinition(abstractServiceType)
                service = serviceDefinition.fetchService() as A
                typeFetchingMonitorer.removeFromBeingFetched(abstractServiceType)
            } catch (e: ServiceProviderException) {
                typeFetchingMonitorer.removeFromBeingFetched(abstractServiceType)
                throw e
            }
            return service
        }

        override fun <A : Any> fetchOrNull(abstractServiceType: KClass<A>): A? {
            return try {
                SP.fetch(abstractServiceType)
            } catch (e: AutowireUnautowirableServiceProviderException) {
                null
            } catch (e: AutowireNoClassFoundServiceProviderException) {
                null
            }
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
                            throw AutowireUnautowirableServiceProviderException(abstractServiceType, implementingClass)
                        } else {
                            implementingClass
                        }
                    }
                    else -> null
                }
                    ?: if (numberOfKClasses == 0) {
                        throw AutowireNoClassFoundServiceProviderException(abstractServiceType)
                    } else {
                        throw AutowireTooManyClassesFoundServiceProviderException(abstractServiceType, numberOfKClasses)
                    }
            } else {
                concreteServiceType = abstractServiceType
            }
            return serviceDefinitionFactory.createByType(
                abstractServiceType,
                concreteServiceType,
                concreteServiceType.getServiceInstanceType()
            ).apply {
                serviceDefinitions[abstractServiceType] = this
            }
        }
    }

    override fun <A : Any> fetch(abstractServiceType: KClass<A>): A {
        return serviceProvider.fetch(abstractServiceType)
    }

    override fun <A : Any> fetchOrNull(abstractServiceType: KClass<A>): A? {
        return serviceProvider.fetchOrNull(abstractServiceType)
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