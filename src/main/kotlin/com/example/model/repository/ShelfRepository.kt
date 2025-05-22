package com.example.model.repository

import com.example.model.entity.Shelf

interface ShelfRepository {
    suspend fun shelvesByDepartment(departmentId: Int): List<Shelf>
    suspend fun addShelf(shelf: Shelf): Shelf
    suspend fun removeShelfById(id: Int): Boolean
    suspend fun updateShelf(shelf: Shelf): Shelf
}