package com.example.model.entity

data class AppUser(
    val id: Int,
    val firebaseUid: String,
    val email: String?,
    val displayName: String?
)