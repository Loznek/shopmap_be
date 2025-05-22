package com.example.db.mapping


import com.example.model.entity.ShoppingList
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID



import org.jetbrains.exposed.dao.id.IntIdTable

object ShoppingListTable : IntIdTable("shopping_list") {
    val userId = integer("user_id")
    val name = varchar("name", 60)
}




class ShoppingListDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ShoppingListDAO>(ShoppingListTable)
    var userId by ShoppingListTable.userId
    var name by ShoppingListTable.name
}

fun daoToModel(dao: ShoppingListDAO) = ShoppingList(
    dao.id.value,
    dao.userId,
    dao.name
)