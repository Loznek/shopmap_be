package com.example.stores.dto

import ShoppingListItemResponse
import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Store
import com.example.model.entity.Till
import com.example.model.entity.WallBlock
import com.example.products.dto.ProductResponse
import kotlinx.serialization.Serializable

@Serializable
data class StoreDetailsResponse(
    val store: Store,
    val map: Map?,
    val departments: List<Department>,
    val wallBlocks: List<WallBlock>,
    val tills: List<Till>
)


@Serializable
data class StoreResponse(
    val id: Int?,
    val name: String,
    val location: String?
)

@Serializable
data class ShoppingListProductMatchResponse(
    val shoppingListItem: ShoppingListItemResponse,
    val product: ProductResponse
)


@Serializable
data class ShoppingListMatchesResponse(
    val shoppingListId: Int,
    val storeId: Int,
    val matches: List<ShoppingListProductMatchResponse>
)