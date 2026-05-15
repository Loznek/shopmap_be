package com.example.stores

import com.example.model.entity.Store
import com.example.model.repository.StoreRepository

class StoreService(
    private val storeRepository: StoreRepository
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
}