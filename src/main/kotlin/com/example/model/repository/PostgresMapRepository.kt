package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.Map
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresMapRepository: MapRepository {
    override suspend fun mapById(id: Int): Map? = suspendTransaction {
        MapDAO.find{(MapTable.id eq id)}.map(::daoToModel).firstOrNull()
    }

    override suspend fun updateMap(map: Map): Map = suspendTransaction {
        val mapDAO = map.id?.let { MapDAO.findById(it) }
            ?: throw IllegalArgumentException("Map with ID ${map.id} not found")

        // Update the DAO fields
        mapDAO.width = map.width
        mapDAO.height = map.height
        mapDAO.entranceX = map.entranceX
        mapDAO.entranceY = map.entranceY
        mapDAO.exitX = map.exitX
        mapDAO.exitY = map.exitY
        mapDAO.storeId = map.storeId

        // Convert the updated DAO back to the Map domain model
        daoToModel(mapDAO)
    }

    override suspend fun addMap(map: Map) : Map = suspendTransaction {
        val newMap = MapDAO.new {
            width = map.width
            height = map.height
            entranceX = map.entranceX
            entranceY = map.entranceY
            exitX = map.exitX
            exitY = map.exitY
            storeId = map.storeId
        }
        daoToModel(newMap)
    }

    override suspend fun removeMap(map: Map): Boolean = suspendTransaction {
        val rowsDeleted = MapTable.deleteWhere {
            MapTable.id eq map.id
        }
        rowsDeleted == 1
    }
}