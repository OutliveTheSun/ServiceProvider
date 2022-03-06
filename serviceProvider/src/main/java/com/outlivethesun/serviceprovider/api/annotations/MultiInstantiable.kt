package com.outlivethesun.serviceprovider.api.annotations

/**
 * When a service is requested that is annotated with [MultiInstantiable], a new instance will be created every time it is instantiated.
 * This can be necessary e.g. for services that keep a state.
 */
@Target(AnnotationTarget.CLASS)
annotation class MultiInstantiable