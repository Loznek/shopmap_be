package com.example.db.mapping

import com.example.model.entity.Map
import com.example.model.entity.Shelf
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ShelfTable : IntIdTable("shelf") {

    val departmentId = integer("departmentid")
    val width = double("width")
    val height = double("height")
    val startX = double("startx")
    val startY = double("starty")
    val midX = double("midx")
    val midY = double("midy")
}

class ShelfDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ShelfDAO>(ShelfTable)
    var departmentId by ShelfTable.departmentId
    var width by ShelfTable.width
    var height by ShelfTable.height
    var startX by ShelfTable.startX
    var startY by ShelfTable.startY
    var midX by ShelfTable.midX
    var midY by ShelfTable.midY

}



fun daoToModel(dao: ShelfDAO) = Shelf(
    dao.id.value,
    dao.departmentId,
    dao.width,
    dao.height,
    dao.startX,
    dao.startY,
    dao.midX,
    dao.midY
)