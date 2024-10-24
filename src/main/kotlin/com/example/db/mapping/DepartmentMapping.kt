package com.example.db.mapping


import com.example.model.entity.Department
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

    object DepartmentTable : IntIdTable("department") {

        val mapId = integer("mapid")
        val name = varchar("name", 50)
        val width = double("width")
        val height = double("height")
        val startX = double("startX")
        val startY = double("startY")
    }

    class DepartmentDAO(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<DepartmentDAO>(DepartmentTable)
        var name by DepartmentTable.name
        var mapId by DepartmentTable.mapId
        var width by DepartmentTable.width
        var height by DepartmentTable.height
        var startX by DepartmentTable.startX
        var startY by DepartmentTable.startY
    }



    fun daoToModel(dao: DepartmentDAO) = Department(
        dao.id.value,
        dao.mapId,
        dao.name,
        dao.width,
        dao.height,
        dao.startX,
        dao.startY
    )