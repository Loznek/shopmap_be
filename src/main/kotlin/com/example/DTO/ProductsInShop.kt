package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
class ProductsInShop (
    val shoppingListId: Int,
    val storeId:Int,
)