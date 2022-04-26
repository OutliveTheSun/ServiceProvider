package com.outlivethesun.serviceprovider.api.annotations

import com.outlivethesun.serviceprovider.api.IServiceProvider

/**
 * This annotation excludes the annotated service class from being instantiated by the [IServiceProvider].
 */
@Target(AnnotationTarget.CLASS)
annotation class Unautowirable