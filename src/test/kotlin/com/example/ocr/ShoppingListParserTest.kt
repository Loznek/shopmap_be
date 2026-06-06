package com.example.ocr.parser

import io.ktor.server.plugins.NotFoundException
import kotlin.test.*

class ShoppingListParserTest {

    private val parser =
        ShoppingListParser()

    @Test
    fun `parseShoppingList parses item pairs`() {

        val result =
            parser.parseShoppingList(
                """
                Milk
                2L
                
                Bread
                Wholegrain
                """.trimIndent()
            )

        assertEquals(2, result.size)

        assertEquals(
            "Milk",
            result[0].shoppingItemName
        )

        assertEquals(
            "2L",
            result[0].attributes
        )

        assertEquals(
            "Bread",
            result[1].shoppingItemName
        )

        assertEquals(
            "Wholegrain",
            result[1].attributes
        )
    }

    @Test
    fun `parseShoppingList handles odd number of lines`() {

        val result =
            parser.parseShoppingList(
                """
                Milk
                2L
                Bread
                """.trimIndent()
            )

        assertEquals(
            2,
            result.size
        )

        assertEquals(
            "",
            result[1].attributes
        )
    }

    @Test
    fun `parseShoppingList ignores blank lines`() {

        val result =
            parser.parseShoppingList(
                """
                
                Milk
                
                2L
                
                """.trimIndent()
            )

        assertEquals(
            1,
            result.size
        )
    }

    @Test
    fun `parseShoppingList throws when no items detected`() {

        assertFailsWith<NotFoundException> {

            parser.parseShoppingList(
                ""
            )
        }
    }
}