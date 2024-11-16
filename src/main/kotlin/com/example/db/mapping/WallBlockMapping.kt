package com.example.db.mapping

import com.example.model.entity.Department
import com.example.model.entity.WallBlock
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object WallBlockTable : IntIdTable("wall_block") {

    val mapId = integer("mapid")
    val width = double("width")
    val height = double("height")
    val startX = double("startx")
    val startY = double("starty")
}

class WallBlockDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WallBlockDAO>(WallBlockTable)
    var mapId by WallBlockTable.mapId
    var width by WallBlockTable.width
    var height by WallBlockTable.height
    var startX by WallBlockTable.startX
    var startY by WallBlockTable.startY
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: WallBlockDAO) = WallBlock(
    dao.id.value,
    dao.mapId,
    dao.width,
    dao.height,
    dao.startX,
    dao.startY
)