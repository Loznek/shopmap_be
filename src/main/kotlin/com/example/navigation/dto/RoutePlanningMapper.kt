package com.example.navigation.dto

import com.example.model.entity.ProductPosition


data class RoutePlanningProduct(
    val articleNo: Int,
    val departmentId: Int,
    val position: ProductPosition
)


fun RoutePlanningProductRequest.toModel() =
    RoutePlanningProduct(
        articleNo = articleNo,
        departmentId = departmentId,
        position = ProductPosition.valueOf(position)
    )