package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
class RoutePlanning (
    val mapId: Int,
    val departmentIds: List<Int>,
    //bevásárlólista
)