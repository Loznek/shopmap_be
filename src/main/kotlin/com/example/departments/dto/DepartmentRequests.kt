package com.example.departments.dto
import io.swagger.v3.oas.annotations.media.Schema

import kotlinx.serialization.Serializable

@Serializable
data class CreateDepartmentRequest(
    val mapId: Int,
    val name: String,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)

@Serializable
data class UpdateDepartmentRequest(
    val id: Int,
    val mapId: Int,
    val name: String,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)