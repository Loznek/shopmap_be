package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.WallBlock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresWallBlockRepository: WallBlockRepository {
    override suspend fun wallBlocksByMap(mapId: Int): List<WallBlock> = suspendTransaction {
        WallBlockDAO.find{(WallBlockTable.mapId eq mapId)}.map(::daoToModel)
    }

    override suspend fun addWallBlock(wallBlock: WallBlock) : Unit = suspendTransaction {
        WallBlockDAO.new {
            width = wallBlock.width
            height = wallBlock.height
            startX = wallBlock.startX
            startY = wallBlock.startY
            mapId = wallBlock.mapId
        }
    }

    override suspend fun removeWallBlockById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = WallBlockTable.deleteWhere {
            WallBlockTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun updateWallBlock(wallBlock: WallBlock) {
        suspendTransaction {
            val wallBlockDAO = wallBlock.id?.let { WallBlockDAO.findById(it) } ?: return@suspendTransaction
            wallBlockDAO.width = wallBlock.width
            wallBlockDAO.height = wallBlock.height
            wallBlockDAO.startX = wallBlock.startX
            wallBlockDAO.startY = wallBlock.startY
            wallBlockDAO.mapId = wallBlock.mapId
        }
    }





}