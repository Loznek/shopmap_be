package com.example.users.dto

import AppUser
import AppUserResponse



fun AppUser.toResponse() = AppUserResponse(
    id = id.toString(),
    firebaseUid = firebaseUid,
    email = email,
    displayName = displayName
)