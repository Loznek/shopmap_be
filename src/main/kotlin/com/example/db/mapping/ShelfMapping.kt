package com.example.db.mapping

import com.example.model.entity.Map
import com.example.model.entity.Shelf
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ShelfTable : IntIdTable("shelf") {

    val shelfId = integer("shelfid")
    val departmentId = integer("departmentId")
    val width = double("width")
    val height = double("height")
    val startX = double("startX")
    val startY = double("startY")
}

class ShelfDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ShelfDAO>(ShelfTable)
    var shelfId by ShelfTable.shelfId
    var departmentId by ShelfTable.departmentId
    var width by ShelfTable.width
    var height by ShelfTable.height
    var startX by ShelfTable.startX
    var startY by ShelfTable.startY

}



fun daoToModel(dao: ShelfDAO) = Shelf(
    dao.shelfId,
    dao.departmentId,
    dao.width,
    dao.height,
    dao.startX,
    dao.startY
)