package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class OpeningHours(
    val id: Int?,
    val storeId: Int,
    val day: Int,
    val openTime: String,
    val closeTime: String
)