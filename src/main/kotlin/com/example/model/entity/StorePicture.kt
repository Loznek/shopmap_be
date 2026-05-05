package com.example.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class StorePicture(
    val id: Int?,
    val storeId: Int,
    val path: String
)