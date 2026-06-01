package com.example.db.mapping

import com.example.model.entity.Product
import com.example.model.entity.ProductPosition

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.postgresql.util.PGobject

object ProductTable : IntIdTable("product", "article_no") {

    val name = varchar("name", 255)

    val size = varchar("size", 100).nullable()

    val departmentId = integer("department_id").nullable()

    val position = customEnumeration(
        name = "position",
        sql = "product_position",
        fromDb = { value ->
            ProductPosition.valueOf(value as String)
        },
        toDb = { value ->
            value.let {
                PGobject().apply {
                    type = "product_position"
                    this.value = it.name
                }
            }
        }
    ).nullable()

    val storeId = integer("store_id")

    val price = double("price").nullable()
}

class ProductDAO(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var name by ProductTable.name
    var size by ProductTable.size
    var departmentId by ProductTable.departmentId
    var position by ProductTable.position
    var storeId by ProductTable.storeId
    var price by ProductTable.price
}

fun daoToModel(dao: ProductDAO): Product =
    Product(
        articleNo = dao.id.value,
        name = dao.name,
        size = dao.size,
        departmentId = dao.departmentId,
        position = dao.position,
        storeId = dao.storeId,
        price = dao.price
    )