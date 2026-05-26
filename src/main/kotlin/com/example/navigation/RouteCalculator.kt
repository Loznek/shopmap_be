package com.example.navigation

import com.example.navigation.optimizer.RouteOptimizer

class RouteCalculator(
    private val distanceMatrixBuilder: DistanceMatrixBuilder,
    private val optimizer: RouteOptimizer,
    private val pathFinder: PathFinder
)