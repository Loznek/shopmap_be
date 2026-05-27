package com.example.db.mapping

import com.example.model.entity.OpeningHours
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object OpeningHoursTable : IntIdTable("opening_hours") {
    val storeId = reference("store_id", StoreTable)
    val day = integer("day")
    val openTime = varchar("open_time", 5)
    val closeTime = varchar("close_time", 5)
}

class OpeningHoursDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OpeningHoursDAO>(OpeningHoursTable)

    var store by StoreDAO referencedOn OpeningHoursTable.storeId
    var day by OpeningHoursTable.day
    var openTime by OpeningHoursTable.openTime
    var closeTime by OpeningHoursTable.closeTime
}


fun openingHoursDaoToModel(dao: OpeningHoursDAO) = OpeningHours(
    dao.id.value,
    dao.store.id.value,
    dao.day,
    dao.openTime,
    dao.closeTime
)