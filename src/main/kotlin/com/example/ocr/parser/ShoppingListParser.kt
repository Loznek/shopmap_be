package com.example.ocr.parser

import com.example.DTO.ConcreteShopItem
import com.example.DTO.ConcreteShopItemTemp
import com.example.DTO.ConcreteShopList
import com.example.DTO.ShopItem
import com.example.shoppingList.dto.CreateShoppingListItemRequest
import kotlinx.serialization.json.Json

class ShoppingListParser {


    fun parseShoppingList(
        text: String
    ): List<CreateShoppingListItemRequest> {

        val lines = text.lines()
            .filter { it.isNotBlank() }

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


    fun parseShopList(responseJson: String): ConcreteShopList {
        val json = Json { ignoreUnknownKeys = true }

        val outerMap = json.decodeFromString<Map<String, String>>(responseJson)

        val answerJson = outerMap["answer"]
            ?: throw IllegalArgumentException("Missing 'answer' field")

        val parsedItems = json.decodeFromString<List<ConcreteShopItemTemp>>(answerJson)

        val items = parsedItems.map {
            ConcreteShopItem(
                name = it.list_item_name,
                productId = it.product_id,
                productName = it.product_name,
                quantity = "1"
            )
        }

        return ConcreteShopList(items)
    }


}