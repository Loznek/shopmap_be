package com.example.db.mapping

import com.example.model.entity.Map
import com.example.model.entity.Product
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProductTable : IntIdTable("map") {

    val articleNo = integer("storeid")
    val name = varchar("name", 30)
    val size = varchar("size", 100)
    val shelfId = integer("shelfid")
    val price = double("price")
    val storeId = integer("storeid")

}

class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)
    var articleNo by ProductTable.articleNo
    var name by  ProductTable.name
    var size by ProductTable.size
    var shelfId by ProductTable.shelfId
    var price by ProductTable.price
    var storeId by ProductTable.storeId
}



fun daoToModel(dao: ProductDAO) = Product(
    dao.articleNo,
    dao.name,
    dao.size,
    dao.shelfId,
    dao.price,
    dao.storeId
)