package com.example.tills.dto

import kotlinx.serialization.Serializable

@Serializable
data class TillResponse(
    val id: Int?,
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)
