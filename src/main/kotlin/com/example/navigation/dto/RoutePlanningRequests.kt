package com.example.navigation.dto


import kotlinx.serialization.Serializable

@Serializable
data class RoutePlanningRequest(
    val mapId: Int,
    val departmentIds: List<Int>
)