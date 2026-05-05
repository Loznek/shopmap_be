package com.example.db.mapping

import com.example.model.entity.StorePicture
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object StorePicturesTable : IntIdTable("store_pictures") {
    val storeId = reference("storeid", StoreTable)
    val path = text("path")
}

class StorePictureDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StorePictureDAO>(StorePicturesTable)

    var store by StoreDAO referencedOn StorePicturesTable.storeId
    var path by StorePicturesTable.path
}

fun storePictureDaoToModel(dao: StorePictureDAO) = StorePicture(
    dao.id.value,
    dao.store.id.value,
    dao.path
)