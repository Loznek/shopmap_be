package com.example.model.repository

import com.example.db.mapping.ShelfDAO
import com.example.db.mapping.ShelfTable
import com.example.db.mapping.daoToModel
import com.example.db.mapping.suspendTransaction
import com.example.model.entity.Shelf

class PostgresShelfRepository: ShelfRepository {

    override suspend fun shelvesByDepartment(departmentId: Int): List<Shelf> = suspendTransaction {
        ShelfDAO.find { ShelfTable.departmentId eq departmentId }
            .map(::daoToModel)
    }

    override suspend fun addShelf(shelf: Shelf): Shelf = suspendTransaction {
        val newShelf = ShelfDAO.new {
            width = shelf.width
            height = shelf.height
            startX = shelf.startX
            startY = shelf.startY
            departmentId = shelf.departmentId
            midX = shelf.midx
            midY = shelf.midy
        }
        daoToModel(newShelf)
    }

    override suspend fun removeShelfById(id: Int): Boolean {
        throw NotImplementedError("Not yet implemented")
    }

    override suspend fun updateShelf(shelf: Shelf): Shelf {
        throw NotImplementedError("Not yet implemented")
    }

}