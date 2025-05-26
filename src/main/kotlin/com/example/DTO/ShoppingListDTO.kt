package com.example.DTO


import kotlinx.serialization.Serializable

@Serializable
class ShopItem(
    val name: String,
    val quantity: String
)

@Serializable
class ShopList(
    val items: List<ShopItem>
)

@Serializable
class ShoppingListWithUserDTO(
    val userId: Int,
    val items: ShopList,
    val listName: String
)

