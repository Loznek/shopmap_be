package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.Department
import com.example.model.entity.WallBlock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresWallBlockRepository: WallBlockRepository {
    override suspend fun wallBlocksByMap(mapId: Int): List<WallBlock> = suspendTransaction {
        WallBlockDAO.find{(WallBlockTable.mapId eq mapId)}.map(::daoToModel)
    }

    override suspend fun addWallBlock(wallBlock: WallBlock) : WallBlock = suspendTransaction {
        val newWallBlock = WallBlockDAO.new {
            width = wallBlock.width
            height = wallBlock.height
            startX = wallBlock.startX
            startY = wallBlock.startY
            mapId = wallBlock.mapId
        }
        daoToModel(newWallBlock)
    }

    override suspend fun removeWallBlockById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = WallBlockTable.deleteWhere {
            WallBlockTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun updateWallBlock(wallBlock: WallBlock): WallBlock = suspendTransaction {
        val wallBlockDAO = wallBlock.id?.let { WallBlockDAO.findById(it) }
            ?: throw IllegalArgumentException("WallBlock with ID ${wallBlock.id} not found")

        // Update the DAO fields
        wallBlockDAO.width = wallBlock.width
        wallBlockDAO.height = wallBlock.height
        wallBlockDAO.startX = wallBlock.startX
        wallBlockDAO.startY = wallBlock.startY
        wallBlockDAO.mapId = wallBlock.mapId

        // Convert the updated DAO back to the WallBlock domain model
        daoToModel(wallBlockDAO)
    }





}