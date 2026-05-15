package com.example.stores.dto

data class CreateStoreRequest(
    val name: String,
    val location: String?
)