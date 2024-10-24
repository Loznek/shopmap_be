package com.example.db.mapping

import com.example.model.entity.Map
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object MapTable : IntIdTable("map") {

    val storeId = integer("storeid")
    val width = double("width")
    val height = double("height")
    val exitX = double("exitx")
    val exitY = double("exity")
    val entranceX = double("entrancex")
    val entranceY = double("entrancey")
}

class MapDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MapDAO>(MapTable)
    var storeId by MapTable.storeId
    var width by MapTable.width
    var height by MapTable.height
    var exitX by MapTable.exitX
    var exitY by MapTable.exitY
    var entranceX by MapTable.entranceX
    var entranceY by MapTable.entranceY
}



fun daoToModel(dao: MapDAO) = Map(
    dao.id.value,
    dao.storeId,
    dao.width,
    dao.height,
    dao.entranceX,
    dao.entranceY,
    dao.exitX,
    dao.exitY
)