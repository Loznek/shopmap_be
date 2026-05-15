package com.example.recipes.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeRequest(
    val mealName: String
)