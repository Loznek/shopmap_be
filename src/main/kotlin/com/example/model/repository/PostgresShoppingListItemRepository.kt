package com.example.model.repository



import com.example.db.mapping.ShoppingListItemDAO
import com.example.db.mapping.ShoppingListItemTable
import com.example.db.mapping.daoToModel
import com.example.db.mapping.suspendTransaction
import com.example.model.entity.ShoppingListItem
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresShoppingListItemRepository : ShoppingListItemRepository {
    override suspend fun itemsByShoppingList(shoppingListId: Int): List<ShoppingListItem> = suspendTransaction {
        ShoppingListItemDAO.find { ShoppingListItemTable.shoppingListId eq shoppingListId }
            .map(::daoToModel)
    }

    override suspend fun addShoppingListItem(item: ShoppingListItem): ShoppingListItem = suspendTransaction {
        val newItem = ShoppingListItemDAO.new {
            shoppingListId = item.shoppingListId
            itemName = item.shoppingItemName
            attributes = item.attributes
        }
        daoToModel(newItem)
    }

    override suspend fun removeShoppingListItemById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = ShoppingListItemTable.deleteWhere { ShoppingListItemTable.id eq id }
        rowsDeleted == 1
    }

    override suspend fun updateShoppingListItem(item: ShoppingListItem): ShoppingListItem = suspendTransaction {
        val dao = ShoppingListItemDAO[item.itemId]
        dao.apply {
            itemName = item.shoppingItemName
            attributes = item.attributes
        }
        daoToModel(dao)
    }
}