package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
data class RoutePlan (
    val route: List<Pair<Int, Int>>,
)