package com.example.db.mapping

import org.jetbrains.exposed.dao.id.IntIdTable
import com.example.model.entity.ShoppingListItem
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object ShoppingListItemTable : IntIdTable("shopping_list_item") {
    val shoppingListId = integer("shopping_list_id").references(ShoppingListTable.id)
    val itemName = varchar("shopping_item_name", 255)
    val attributes = varchar("attributes", 255)
}

class ShoppingListItemDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ShoppingListItemDAO>(ShoppingListItemTable)
    var shoppingListId by ShoppingListItemTable.shoppingListId
    var itemName by ShoppingListItemTable.itemName
    var attributes by ShoppingListItemTable.attributes
}

fun daoToModel(dao: ShoppingListItemDAO) = ShoppingListItem(
    dao.id.value,
    dao.shoppingListId,
    dao.itemName,
    dao.attributes
)