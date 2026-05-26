package com.example.navigation

import com.example.navigation.optimizer.HeldKarpSolver
import com.example.navigation.optimizer.NearestNeighborSolver
import com.example.navigation.optimizer.RouteOptimizer

class RouteOptimizerFactory(
    private val heldKarpSolver: HeldKarpSolver,
    private val nearestNeighborSolver: NearestNeighborSolver
) {

    fun getOptimizer(
        destinationCount: Int
    ): RouteOptimizer {

        return if (destinationCount <= 15) {
            heldKarpSolver
        } else {
            nearestNeighborSolver
        }
    }
}