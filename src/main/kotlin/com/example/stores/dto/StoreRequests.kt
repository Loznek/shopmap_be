package com.example.stores.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateStoreRequest(
    val name: String,
    val location: String?
)

@Serializable
data class UpdateStoreRequest(
    val id: Int,
    val name: String,
    val location: String?
)


