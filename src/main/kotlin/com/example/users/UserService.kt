package com.example.users

import com.example.model.entity.AppUser
import com.example.model.repository.UserRepository
import com.example.plugins.FirebaseUserPrincipal

class UserService(
    private val repository: UserRepository
) {

    suspend fun getCurrentUser(
        principal: FirebaseUserPrincipal
    ): AppUser {

        return repository.getByFirebaseUid(principal.uid)
            ?: repository.create(
                firebaseUid = principal.uid,
                email = principal.email,
                displayName = principal.displayName ?: "No UserName"
            )
    }
}