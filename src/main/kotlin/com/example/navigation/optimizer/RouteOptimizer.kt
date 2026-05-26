package com.example.navigation.optimizer

interface RouteOptimizer {

    fun solveOrder(
        distances: Array<IntArray>,
        destinationCount: Int,
        endIndex: Int
    ): List<Int>
}