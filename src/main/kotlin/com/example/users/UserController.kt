package com.example.users
import AppUserResponse
import FirebaseUserPrincipal
import UserService
import com.example.users.dto.toResponse


class UserController(
    private val userService: UserService
) {

    suspend fun me(
        principal: FirebaseUserPrincipal
    ): AppUserResponse {

        return userService.getCurrentUser(principal).toResponse()

    }
}