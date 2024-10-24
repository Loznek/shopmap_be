package com.example.model.repository;

import com.example.db.mapping.*
import com.example.model.entity.Till

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresTillRepository : TillRepository {


    override suspend fun tillsByMap(mapId: Int): List<Till> = suspendTransaction {
        TillDAO.find { (WallBlockTable.mapId eq mapId) }.map(::daoToModel)
    }

    override suspend fun addTill(till: Till): Unit = suspendTransaction {
        TillDAO.new {
            width = till.width
            height = till.height
            startX = till.startX
            startY = till.startY
            mapId = till.mapId
        }
    }

    override suspend fun removeTillById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = TillTable.deleteWhere {
            TillTable.id eq id
        }
        rowsDeleted == 1
    }

}
