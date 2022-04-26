package com.outlivethesun.serviceprovider.api

import com.outlivethesun.serviceprovider.internal.ServiceProvider

val SP: IServiceProvider by lazy { ServiceProvider() }