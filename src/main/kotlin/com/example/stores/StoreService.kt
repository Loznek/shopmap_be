package com.example.stores


import com.example.model.entity.Store
import com.example.model.repository.*
import com.example.products.dto.toResponse
import com.example.repository.ShoppingListItemRepository
import com.example.repository.ShoppingListRepository
import com.example.shoppingList.dto.toResponse
import com.example.stores.dto.ShoppingListMatchesResponse
import com.example.stores.dto.ShoppingListProductMatchResponse
import com.example.stores.dto.StoreDetailsResponse

class StoreService(
    private val storeRepository: StoreRepository,
    private val mapRepository: MapRepository,
    private val departmentRepository: DepartmentRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val tillRepository: TillRepository,
    private val shoppingListItemRepository: ShoppingListItemRepository,
    private val productRepository: ProductRepository
) {

    suspend fun getById(id: Int): Store {
        return storeRepository.storeById(id)
            ?: throw IllegalArgumentException("Store not found")
    }

    suspend fun delete(id: Int) {
        val store = storeRepository.storeById(id)
            ?: throw IllegalArgumentException("Store not found")

        storeRepository.removeStore(store)
    }

    suspend fun create(store: Store): Store {
        return storeRepository.addStore(store)
    }

    suspend fun getMatches(
        storeId: Int,
        shoppingListId: Int
    ): ShoppingListMatchesResponse {



        val items =
            shoppingListItemRepository.getShoppingListItems(shoppingListId)

        val products =
            productRepository
                .productsByStoreId(storeId)

        val productMap =
            products.associateBy {
                it.name.trim().lowercase()
            }

        val matches =
            items.mapNotNull { item ->

                val product =
                    productMap[
                        item.shoppingItemName
                            .trim()
                            .lowercase()
                    ]

                product?.let {
                    ShoppingListProductMatchResponse(
                        shoppingListItem = item.toResponse(),
                        product = it.toResponse()
                    )
                }
            }

        return ShoppingListMatchesResponse(
            shoppingListId = shoppingListId,
            storeId = storeId,
            matches = matches
        )
    }



    suspend fun getStoreDetails(storeId: Int): StoreDetailsResponse {
        val store = storeRepository.storeById(storeId)
            ?: throw IllegalArgumentException("Store not found")

        val map = mapRepository.mapsByStoreId(storeId).firstOrNull()

        if (map == null) {
            return StoreDetailsResponse(
                store = store,
                map = null,
                departments = emptyList(),
                wallBlocks = emptyList(),
                tills = emptyList()
            )
        }

        val departments = departmentRepository.departmentsByMap(map.id!!)
        val wallBlocks = wallBlockRepository.wallBlocksByMap(map.id!!)
        val tills = tillRepository.tillsByMap(map.id!!)

        return StoreDetailsResponse(
            store = store,
            map = map,
            departments = departments,
            wallBlocks = wallBlocks,
            tills = tills
        )
    }

}