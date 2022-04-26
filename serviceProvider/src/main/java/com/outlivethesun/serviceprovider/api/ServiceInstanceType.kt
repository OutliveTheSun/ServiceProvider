package com.outlivethesun.serviceprovider.api

/**
 * This enum class contains all variations of how a service can be initialized.
 * [SINGLE_INSTANTIABLE] means that the service can only be created once.
 * [MULTI_INSTANTIABLE] means that each request creates a new instance of the service.
 */
enum class ServiceInstanceType {
    SINGLE_INSTANTIABLE,
    MULTI_INSTANTIABLE
}