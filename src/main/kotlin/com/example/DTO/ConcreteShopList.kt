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


// Temporary DTO matching the JSON structure
@Serializable
data class ConcreteShopItemTemp(
    val list_item_name: String,
    val product_id: Int? = null,
    val product_name: String? = null
)