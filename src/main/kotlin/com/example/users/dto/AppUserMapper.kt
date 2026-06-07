package com.example.users.dto

import com.example.model.entity.AppUser


fun AppUser.toResponse() = AppUserResponse(
    id = id.toString(),
    firebaseUid = firebaseUid,
    email = email,
    displayName = displayName
)