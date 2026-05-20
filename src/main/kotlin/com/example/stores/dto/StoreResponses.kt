package com.example.stores.dto

import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Store
import com.example.model.entity.Till
import com.example.model.entity.WallBlock
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