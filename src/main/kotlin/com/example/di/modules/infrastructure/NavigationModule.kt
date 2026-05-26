package com.example.di.modules.infrastructure

import com.example.navigation.DistanceMatrixBuilder
import com.example.navigation.GridBuilder
import com.example.navigation.PathFinder
import com.example.navigation.PathValidator
import com.example.navigation.RouteOptimizerFactory
import com.example.navigation.optimizer.HeldKarpSolver
import com.example.navigation.optimizer.NearestNeighborSolver
import com.example.navigation.optimizer.TwoOptSolver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationModule = module {

    singleOf(::PathFinder)

    singleOf(::PathValidator)

    singleOf(::GridBuilder)

    singleOf(::DistanceMatrixBuilder)

    singleOf(::HeldKarpSolver)

    singleOf(::TwoOptSolver)

    singleOf(::NearestNeighborSolver)

    singleOf(::RouteOptimizerFactory)
}