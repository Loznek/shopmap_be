package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
data class AiRequest(
    val products: List<Pair<Int?, String>>, // Pair of product ID and product name
    val listItems: List<String>
)