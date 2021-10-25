package com.outlivethesun.serviceprovider

import com.outlivethesun.serviceprovider.classloader.ReflectionsInfo
import kotlin.reflect.KClass

object SP : IServiceProvider {
    private val serviceProvider = ServiceProviderDefault()

    private class ServiceProviderDefault : IServiceProvider {
        private val serviceDefinitionFactory by lazy { ServiceDefinitionFactory() }
        private val serviceDefinitions = mutableMapOf<KClass<Any>, ServiceDefinition<*>>()
        private val reflectionsInfo by lazy { ReflectionsInfo() }

        override fun <A : Any> getService(abstractServiceType: KClass<out A>): A {
            val serviceInstance =
                serviceDefinitions[abstractServiceType as Any] ?: loadServiceDefinition(abstractServiceType)
            return serviceInstance.createService() as A
        }

        override fun <A : Any> setService(abstractServiceType: KClass<out A>, service: A) {
            serviceDefinitions[abstractServiceType as KClass<Any>] =
                serviceDefinitionFactory.createByInstance(service)
        }

        private fun <A : Any> loadServiceDefinition(abstractServiceType: KClass<A>): ServiceDefinition<A> {
            val listOfKClasses = reflectionsInfo.findImplementingClassesOfInterface(abstractServiceType)
            val concreteServiceType = when (listOfKClasses.size) {
                1 -> listOfKClasses.first()
                else -> throw RuntimeException("Unable to create service \"${abstractServiceType.simpleName}\". ${listOfKClasses.size} classes found to autowire.")
            }
            val serviceDefinition = serviceDefinitionFactory.createByType<A>(concreteServiceType)
            serviceDefinitions[abstractServiceType as KClass<Any>] = serviceDefinition
            return serviceDefinition
        }
    }

    override fun <A : Any> getService(abstractServiceType: KClass<out A>): A {
        return serviceProvider.getService(abstractServiceType)
    }

    override fun <A : Any> setService(abstractServiceType: KClass<out A>, service: A) {
        serviceProvider.setService(abstractServiceType, service)
    }

}