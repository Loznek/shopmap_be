package com.example.model.repository

import com.example.model.entity.AppUser

interface UserRepository {

    suspend fun getByFirebaseUid(firebaseUid: String): AppUser?

    suspend fun create(
        firebaseUid: String,
        email: String?,
        displayName: String?
    ): AppUser
}