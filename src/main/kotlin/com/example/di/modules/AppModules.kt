package com.example.di.modules


import com.example.di.modules.infrastructure.googleMapsModule

import com.example.di.modules.infrastructure.httpClientModule
import com.example.di.modules.infrastructure.navigationModule
import com.example.di.modules.infrastructure.ocrModule
import com.example.di.modules.infrastructure.salesModule

import di.modules.service.serviceModule

val appModules = listOf(
    repositoryModule,
    httpClientModule,
    navigationModule,
    ocrModule,
    salesModule,
    googleMapsModule,
    serviceModule,
    controllerModule
)