package com.example.db.mapping

import com.example.model.entity.ShoppingList
import com.example.model.mapping.AppUserDAO
import com.example.model.mapping.AppUserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ShoppingListTable : IntIdTable("shopping_list") {

    val user =
        reference(
            "user_id",
            AppUserTable
        )

    val name =
        text("name")
}

class ShoppingListDAO(id: EntityID<Int>) : IntEntity(id) {

    companion object :
        IntEntityClass<ShoppingListDAO>(
            ShoppingListTable
        )

    var user by AppUserDAO referencedOn
            ShoppingListTable.user

    var name by ShoppingListTable.name
}

fun daoToModel(
    dao: ShoppingListDAO
) = ShoppingList(
    id = dao.id.value,
    userId = dao.user.id.value,
    name = dao.name
)