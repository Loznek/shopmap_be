package com.example.maps.dto

data class CreateMapRequest(
    val width: Double,
    val height: Double,
    val entranceX: Double,
    val entranceY: Double,
    val exitX: Double,
    val exitY: Double,
    val storeId: Int
)

data class UpdateMapRequest(
    val id: Int,
    val width: Double,
    val height: Double,
    val entranceX: Double,
    val entranceY: Double,
    val exitX: Double,
    val exitY: Double,
    val storeId: Int
)