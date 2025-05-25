package com.example.model.repository

import com.example.db.mapping.ShoppingListDAO
import com.example.db.mapping.ShoppingListTable
import com.example.db.mapping.daoToModel
import com.example.db.mapping.suspendTransaction
import com.example.model.entity.ShoppingList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresShoppingListRepository : ShoppingListRepository {
    override suspend fun shoppingListsByUser(userId: Int): List<ShoppingList> = suspendTransaction {
        ShoppingListDAO.find { ShoppingListTable.userId eq userId }
            .map(::daoToModel)
    }

    override suspend fun shoppingListById(id: Int): ShoppingList? = suspendTransaction {
        ShoppingListDAO.find { ShoppingListTable.id eq id }
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addShoppingList(shoppingList: ShoppingList): ShoppingList = suspendTransaction {
        val newShoppingList = ShoppingListDAO.new {
            name = shoppingList.name
            userId = shoppingList.userId
        }
        daoToModel(newShoppingList)
    }

    override suspend fun removeShoppingListById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = ShoppingListTable.deleteWhere { ShoppingListTable.id eq id }
        rowsDeleted == 1
    }
}