package com.example.sales.dto

import kotlinx.serialization.Serializable





@Serializable

data class SalesResponse(
    val store: String,
    val validFrom: String,
    val validTo: String,
    val offers: List<String>
)