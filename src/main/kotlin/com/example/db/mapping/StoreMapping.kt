package com.example.db.mapping

import com.example.model.entity.Department
import com.example.model.entity.Store
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object StoreTable : IntIdTable("store") {


    val name = varchar("name", 50)
    val location = varchar("location", 50)
}

class StoreDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StoreDAO>(StoreTable)
    var name by StoreTable.name
    var location by StoreTable.location
}



fun daoToModel(dao: StoreDAO) = Store(
    dao.id.value,
    dao.name,
    dao.location
)