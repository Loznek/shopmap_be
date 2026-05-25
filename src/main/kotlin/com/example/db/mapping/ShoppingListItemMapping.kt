package com.example.db.mapping

import com.example.model.entity.ShoppingListItem
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ShoppingListItemTable : IntIdTable("shopping_list_item") {

    val shoppingList =
        reference(
            "shoppinglistid",
            ShoppingListTable
        )

    val itemName =
        varchar("shoppingitem_name", 255)

    val attributes =
        varchar("attributes", 255)
            .nullable()
}

class ShoppingListItemDAO(id: EntityID<Int>) : IntEntity(id) {

    companion object :
        IntEntityClass<ShoppingListItemDAO>(
            ShoppingListItemTable
        )

    var shoppingList by ShoppingListDAO referencedOn
            ShoppingListItemTable.shoppingList

    var itemName by ShoppingListItemTable.itemName

    var attributes by ShoppingListItemTable.attributes
}

fun daoToModel(
    dao: ShoppingListItemDAO
) = ShoppingListItem(
    itemId = dao.id.value,
    shoppingListId = dao.shoppingList.id.value,
    shoppingItemName = dao.itemName,
    attributes = dao.attributes ?: ""
)