package com.example.ocr.parser

import com.example.DTO.ConcreteShopItem
import com.example.DTO.ConcreteShopItemTemp
import com.example.DTO.ConcreteShopList
import com.example.DTO.ShopItem
import com.example.shoppingList.dto.CreateShoppingListItemRequest
import io.ktor.server.plugins.NotFoundException
import kotlinx.serialization.json.Json

class ShoppingListParser {


    fun parseShoppingList(
        text: String
    ): List<CreateShoppingListItemRequest> {

        val lines = text.lines()
            .filter { it.isNotBlank() }

        if (lines.isEmpty()) {

            throw NotFoundException(
                "No shopping list items detected"
            )
        }

        val items =
            mutableListOf<CreateShoppingListItemRequest>()

        var i = 0

        while (i < lines.size - 1) {

            items.add(
                CreateShoppingListItemRequest(
                    shoppingItemName = lines[i].trim(),
                    attributes = lines[i + 1].trim()
                )
            )

            i += 2
        }

        if (i == lines.lastIndex) {

            items.add(
                CreateShoppingListItemRequest(
                    shoppingItemName = lines[i].trim(),
                    attributes = ""
                )
            )
        }

        return items
    }

}