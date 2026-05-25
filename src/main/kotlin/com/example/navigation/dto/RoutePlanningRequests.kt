package com.example.navigation.dto


import com.example.model.entity.ProductPosition
import kotlinx.serialization.Serializable



@Serializable
data class RoutePlanningRequest(
    val mapId: Int,
    val products: List<RoutePlanningProductRequest>
)

@Serializable
data class RoutePlanningProductRequest(
    val articleNo: Int,
    val departmentId: Int,
    val position: String
)