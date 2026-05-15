package com.example.recipes.dto

import kotlinx.serialization.Serializable

@Serializable
data class IngredientResponse(
    val name: String,
    val amount: String
)