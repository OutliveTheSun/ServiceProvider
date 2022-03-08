package com.outlivethesun.serviceprovider.internal.serviceDefinition

import kotlin.reflect.KClass

internal interface IServiceDefinitionDictionary {
    fun <A : Any> fetch(abstractServiceType: KClass<A>): ServiceDefinition<A>
}