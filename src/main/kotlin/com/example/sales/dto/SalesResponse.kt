package com.example.sales.dto

import kotlinx.serialization.Serializable

@Serializable
data class SalesResponse(
    val items: List<SalesItem>
)