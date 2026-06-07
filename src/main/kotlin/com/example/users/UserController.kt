package com.example.users
import com.example.users.dto.AppUserResponse
import com.example.plugins.FirebaseUserPrincipal
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