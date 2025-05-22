package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
class ConcreteShopList(
    val items: List<ConcreteShopItem>
)

@Serializable
class ConcreteShopItem(
    val name: String,
    val productName: String?,
    val productId: Int?,
    val quantity: String
)