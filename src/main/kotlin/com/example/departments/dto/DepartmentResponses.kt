package com.example.departments.dto

data class DepartmentResponse(
    val id: Int?,
    val name: String,
    val mapId: Int,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)