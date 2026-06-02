package com.example.maps.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProcessImageRequest(
    val mapWidth: Double,
    val mapHeight: Double,
    val mapId: Int
)