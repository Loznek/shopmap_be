package com.example.departments.dto

import com.example.model.entity.Department

fun CreateDepartmentRequest.toEntity() = Department(
    id = null,
    mapId = mapId,
    name = name,
    startX = startX,
    startY = startY,
    width = width,
    height = height
)

fun UpdateDepartmentRequest.toEntity() = Department(
    id = id,
    mapId = mapId,
    name = name,
    startX = startX,
    startY = startY,
    width = width,
    height = height
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