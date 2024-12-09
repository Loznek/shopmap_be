package com.example.model.repository;

import com.example.db.mapping.*
import com.example.model.entity.Till

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresTillRepository : TillRepository {


    override suspend fun tillsByMap(mapId: Int): List<Till> = suspendTransaction {
        TillDAO.find { (TillTable.mapId eq mapId) }.map(::daoToModel)
    }

    override suspend fun addTill(till: Till): Till = suspendTransaction {
        val till= TillDAO.new {
            width = till.width
            height = till.height
            startX = till.startX
            startY = till.startY
            mapId = till.mapId
        }
        daoToModel(till)
    }

    override suspend fun removeTillById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = TillTable.deleteWhere {
            TillTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun updateTill(till: Till): Till = suspendTransaction {
        val tillDAO = till.id?.let { TillDAO.findById(it) }
            ?: throw IllegalArgumentException("Till with ID ${till.id} not found")

        // Update the DAO fields
        tillDAO.width = till.width
        tillDAO.height = till.height
        tillDAO.startX = till.startX
        tillDAO.startY = till.startY
        tillDAO.mapId = till.mapId

        // Convert the updated DAO back to the Till domain model
        daoToModel(tillDAO)
    }

}
