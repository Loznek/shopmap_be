package com.example.db.mapping

import com.example.model.entity.Till
import com.example.model.entity.WallBlock
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object TillTable : IntIdTable("till") {

    val mapId = integer("mapid")
    val width = double("width")
    val height = double("height")
    val startX = double("startX")
    val startY = double("startY")
}

class TillDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TillDAO>(TillTable)
    var mapId by TillTable.mapId
    var width by TillTable.width
    var height by TillTable.height
    var startX by TillTable.startX
    var startY by TillTable.startY
}

fun daoToModel(dao: TillDAO) = Till(
    dao.id.value,
    dao.mapId,
    dao.width,
    dao.height,
    dao.startX,
    dao.startY
)