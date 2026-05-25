package com.example.maps.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProcessImageRequest(
    val mapWidth: Int,
    val mapHeight: Int,
    val mapId: Int
)