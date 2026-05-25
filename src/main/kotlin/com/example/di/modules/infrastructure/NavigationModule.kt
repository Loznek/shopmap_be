package com.example.di.modules.infrastructure

import org.koin.core.module.dsl.singleOf


import com.example.navigation.GridBuilder
import com.example.navigation.PathFinder
import com.example.navigation.PathValidator
import com.example.maps.PythonMapProcessorClient
import org.koin.dsl.module

val navigationModule = module {

    singleOf(::GridBuilder)

    singleOf(::PathFinder)

    singleOf(::PathValidator)

    singleOf(::PythonMapProcessorClient)
}