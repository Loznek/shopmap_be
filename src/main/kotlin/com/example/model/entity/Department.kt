package com.example.model.entity

import kotlinx.serialization.Serializable


@Serializable
data class Department(
    var id: Int?,
    val mapId: Int,
    val name: String, // Nullable as per the diagram
    val width: Double,
    val height: Double,
    val startX: Double,
    val startY: Double
)