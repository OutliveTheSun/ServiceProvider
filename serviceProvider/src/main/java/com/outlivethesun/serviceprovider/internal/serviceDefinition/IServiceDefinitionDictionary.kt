package com.outlivethesun.serviceprovider.internal.serviceDefinition

import kotlin.reflect.KClass

/**
 * An implementation of this interface is responsible for fetching an [IServiceDefinition] and therefore checking for
 * a valid class constellation implementing the requested service type.
 * In case of errors, a [ServiceDefinitionDictionaryException] is thrown.
 */
internal interface IServiceDefinitionDictionary {
    fun <T : Any> fetch(abstractServiceType: KClass<T>): IServiceDefinition<T>
}