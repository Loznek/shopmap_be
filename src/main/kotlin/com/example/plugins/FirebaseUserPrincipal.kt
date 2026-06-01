package com.example.plugins

data class FirebaseUserPrincipal(
    val uid: String,
    val email: String?,
    val displayName: String?
)