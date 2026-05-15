package com.example.navigation.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoutePlanResponse(
    val route: List<Pair<Int, Int>>
)