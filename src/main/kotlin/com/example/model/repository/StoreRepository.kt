package com.example.model.repository

import com.example.model.entity.Department
import com.example.model.entity.Store

interface StoreRepository {
    suspend fun storeById(id:Int): Store?
    suspend fun addStore(store: Store): Store
    suspend fun removeStore(id:Int): Boolean
    suspend fun getAll(): List<Store>
    suspend fun update(store: Store): Store

}