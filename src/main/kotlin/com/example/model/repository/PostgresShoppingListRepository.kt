package com.example.repository

import com.example.db.mapping.*
import com.example.model.entity.ShoppingList
import com.example.model.mapping.AppUserDAO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresShoppingListRepository :
    ShoppingListRepository {

    override suspend fun addShoppingList(
        shoppingList: ShoppingList
    ): ShoppingList = suspendTransaction {

        val dao = ShoppingListDAO.new {

            user = AppUserDAO[shoppingList.userId]

            name = shoppingList.name
        }

        daoToModel(dao)
    }

    override suspend fun getShoppingList(
        id: Int
    ): ShoppingList? = suspendTransaction {

        ShoppingListDAO
            .findById(id)
            ?.let(::daoToModel)
    }

    override suspend fun getShoppingListsByUser(
        userId: Int
    ): List<ShoppingList> = suspendTransaction {

        ShoppingListDAO.find {
            ShoppingListTable.user eq userId
        }
            .map(::daoToModel)
    }

    override suspend fun updateShoppingList(
        shoppingList: ShoppingList
    ): ShoppingList = suspendTransaction {

        val dao =
            ShoppingListDAO[shoppingList.id!!]

        dao.name = shoppingList.name

        daoToModel(dao)
    }

    override suspend fun deleteShoppingList(
        id: Int
    ) {
        suspendTransaction {

            ShoppingListDAO
                .findById(id)
                ?.delete()
        }
    }
}