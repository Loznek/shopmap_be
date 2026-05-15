package com.example.departments.dto

data class CreateDepartmentRequest(
    val mapId: Int,
    val name: String,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)

data class UpdateDepartmentRequest(
    val id: Int,
    val mapId: Int,
    val name: String,
    val startX: Double,
    val startY: Double,
    val width: Double,
    val height: Double
)