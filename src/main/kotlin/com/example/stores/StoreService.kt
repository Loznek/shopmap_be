package com.example.stores

import com.example.model.entity.Store
import com.example.model.repository.*
import com.example.stores.dto.StoreDetailsResponse

class StoreService(
    private val storeRepository: StoreRepository,
    private val mapRepository: MapRepository,
    private val departmentRepository: DepartmentRepository,
    private val wallBlockRepository: WallBlockRepository,
    private val tillRepository: TillRepository
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