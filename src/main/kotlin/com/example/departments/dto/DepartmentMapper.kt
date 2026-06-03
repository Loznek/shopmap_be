package com.example.departments.dto

import com.example.model.entity.Department
import com.example.navigation.roundToHalf

fun CreateDepartmentRequest.toEntity() = Department(
    id = null,
    mapId = mapId,
    name = name,
    startX = startX.roundToHalf(),
    startY = startY.roundToHalf(),
    width = width.roundToHalf(),
    height = height.roundToHalf()
)

fun UpdateDepartmentRequest.toEntity() = Department(
    id = id,
    mapId = mapId,
    name = name,
    startX = startX.roundToHalf(),
    startY = startY.roundToHalf(),
    width = width.roundToHalf(),
    height = height.roundToHalf()
)

fun Department.toResponse() = DepartmentResponse(
    id = id,
    name = name,
    mapId = mapId,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)

