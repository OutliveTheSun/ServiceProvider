package com.outlivethesun.serviceprovider.api

import com.outlivethesun.reflectioninfo.IReflectionInfo
import com.outlivethesun.reflectioninfo.ReflectionInfo
import com.outlivethesun.serviceprovider.api.exceptions.NoClassFoundServiceProviderException
import com.outlivethesun.serviceprovider.api.exceptions.ServiceProviderException
import com.outlivethesun.serviceprovider.api.exceptions.UnautowirableServiceProviderException
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionDictionary
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinition
import com.outlivethesun.serviceprovider.internal.serviceDefinition.ServiceDefinitionFactory
import com.outlivethesun.serviceprovider.internal.typeFetchingTracker.ITypeFetchingTracker
import com.outlivethesun.serviceprovider.internal.typeFetchingTracker.TypeFetchingTracker
import kotlin.reflect.KClass

object SP : IServiceProvider {
    private val serviceProvider = ServiceProviderDefault()

    @Suppress("UNCHECKED_CAST")
    private class ServiceProviderDefault : IServiceProvider {
        private val serviceDefinitionFactory by lazy { ServiceDefinitionFactory() }
        private val serviceDefinitionDictionary by lazy { ServiceDefinitionDictionary(serviceDefinitionFactory, reflectionInfo) }
        private val serviceDefinitions = mutableMapOf<KClass<*>, ServiceDefinition<*>>()
        private val typeFetchingTracker: ITypeFetchingTracker by lazy { TypeFetchingTracker() }

        //boot load ReflectionInfo
        private val reflectionInfo by lazy { ReflectionInfo() }.also { reflectionInfoLazy ->
            put<IReflectionInfo>(reflectionInfoLazy.value)
        }

        override fun <A : Any> fetch(abstractServiceType: KClass<A>): A {
            /**
             * Check if a service was already requested to avoid endless loop for circular reference
             */
            typeFetchingTracker.checkIsNotAlreadyTracking(abstractServiceType)
            val service: A
            try {
                typeFetchingTracker.startTracking(abstractServiceType)
                val serviceDefinition =
                    serviceDefinitions[abstractServiceType] ?: fetchServiceDefinitionFromDictionary(abstractServiceType)
                service = serviceDefinition.fetchService() as A
                typeFetchingTracker.stopTracking(abstractServiceType)
            } catch (e: ServiceProviderException) {
                typeFetchingTracker.stopTracking(abstractServiceType)
                throw e
            }
            return service
        }

        override fun <A : Any> fetchOrNull(abstractServiceType: KClass<A>): A? {
            return try {
                SP.fetch(abstractServiceType)
            } catch (e: UnautowirableServiceProviderException) {
                null
            } catch (e: NoClassFoundServiceProviderException) {
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

        private fun <A : Any> fetchServiceDefinitionFromDictionary(abstractServiceType: KClass<A>): ServiceDefinition<A> {
            return serviceDefinitionDictionary.fetch(abstractServiceType).also {
                serviceDefinitions[abstractServiceType] = it
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