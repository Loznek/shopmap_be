package com.example.repository

import com.example.db.mapping.*
import com.example.model.entity.ShoppingListItem
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresShoppingListItemRepository :
    ShoppingListItemRepository {

    override suspend fun addShoppingListItem(
        item: ShoppingListItem
    ): ShoppingListItem = suspendTransaction {

        val dao = ShoppingListItemDAO.new {

            shoppingList =
                ShoppingListDAO[item.shoppingListId]

            itemName =
                item.shoppingItemName

            attributes =
                item.attributes
        }

        daoToModel(dao)
    }

    override suspend fun getShoppingListItems(
        shoppingListId: Int
    ): List<ShoppingListItem> = suspendTransaction {

        ShoppingListItemDAO.find {
            ShoppingListItemTable.shoppingList eq shoppingListId
        }
            .map(::daoToModel)
    }

    override suspend fun updateShoppingListItem(
        item: ShoppingListItem
    ): ShoppingListItem = suspendTransaction {

        val dao =
            ShoppingListItemDAO[item.itemId!!]

        dao.itemName =
            item.shoppingItemName

        dao.attributes =
            item.attributes

        daoToModel(dao)
    }

    override suspend fun deleteShoppingListItem(
        itemId: Int
    ) {
        suspendTransaction {

            ShoppingListItemDAO
                .findById(itemId)
                ?.delete()
        }
    }
}