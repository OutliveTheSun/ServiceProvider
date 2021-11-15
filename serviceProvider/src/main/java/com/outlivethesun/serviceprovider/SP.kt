package com.outlivethesun.serviceprovider

import com.outlivethesun.serviceprovider.classloader.ReflectionsInfo
import kotlin.reflect.KClass

object SP : IServiceProvider {
    private val serviceProvider = ServiceProviderDefault()

    private class ServiceProviderDefault : IServiceProvider {
        private val serviceDefinitionFactory by lazy { ServiceDefinitionFactory() }
        private val serviceDefinitions = mutableMapOf<KClass<Any>, ServiceDefinition<*>>()
        private val reflectionsInfo by lazy { ReflectionsInfo() }

        override fun <A : Any> fetch(abstractServiceType: KClass<out A>): A {
            val serviceInstance =
                serviceDefinitions[abstractServiceType as Any] ?: autowireServiceDefinition(abstractServiceType)
            return serviceInstance.grabService() as A
        }

        override fun <A : Any> put(abstractServiceType: KClass<out A>, service: A) {
            serviceDefinitions[abstractServiceType as KClass<Any>] =
                serviceDefinitionFactory.createByInstance(abstractServiceType, service)
        }

        override fun <A : Any> remove(abstractServiceType: KClass<out A>) {
            serviceDefinitions.remove(abstractServiceType as KClass<Any>)
        }

        private fun <A : Any> autowireServiceDefinition(abstractServiceType: KClass<A>): ServiceDefinition<A> {
            val listOfKClasses = reflectionsInfo.findImplementingClassesOfInterface(abstractServiceType)
            val concreteServiceType = when (listOfKClasses.size) {
                1 -> listOfKClasses.first()
                else -> throw RuntimeException("Unable to create service \"${abstractServiceType.simpleName}\". ${listOfKClasses.size} classes found to autowire.")
            }
            val serviceDefinition = serviceDefinitionFactory.createByType<A>(
                abstractServiceType,
                concreteServiceType,
                ServiceInstanceType.SINGLE_INSTANCEABLE
            )
            serviceDefinitions[abstractServiceType as KClass<Any>] = serviceDefinition
            return serviceDefinition
        }
    }

    override fun <A : Any> fetch(abstractServiceType: KClass<out A>): A {
        return serviceProvider.fetch(abstractServiceType)
    }

    override fun <A : Any> put(abstractServiceType: KClass<out A>, service: A) {
        serviceProvider.put(abstractServiceType, service)
    }

    override fun <A : Any> remove(abstractServiceType: KClass<out A>) {
        serviceProvider.remove(abstractServiceType)
    }

}