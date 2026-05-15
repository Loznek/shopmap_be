package com.example.stores.dto

import com.example.model.entity.Store

fun CreateStoreRequest.toEntity() = Store(
    id = null,
    name = name,
    location = location
)

fun Store.toResponse() = StoreResponse(
    id = id,
    name = name,
    location = location
)