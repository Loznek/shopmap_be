package com.example.ocr

import com.example.exception.ValidationException
import com.example.ocr.parser.ShoppingListParser
import com.example.ocr.providers.GoogleDocumentAiProvider
import com.example.ocr.providers.TesseractOcrProvider
import com.example.shoppingList.dto.CreateShoppingListItemRequest
import io.mockk.every
import io.mockk.mockk
import kotlin.test.*

class OcrServiceTest {

    private val tesseractProvider =
        mockk<TesseractOcrProvider>()

    private val googleProvider =
        mockk<GoogleDocumentAiProvider>()

    private val parser =
        mockk<ShoppingListParser>()

    private val service =
        OcrService(
            tesseractProvider,
            googleProvider,
            parser
        )

    @Test
    fun `extractShoppingListWithGoogle throws ValidationException for empty file`() {

        assertFailsWith<ValidationException> {

            service.extractShoppingListWithGoogle(
                byteArrayOf()
            )
        }
    }

    @Test
    fun `extractShoppingListWithGoogle delegates to providers`() {

        val expected =
            listOf(
                CreateShoppingListItemRequest(
                    shoppingItemName = "Milk",
                    attributes = "2L"
                )
            )

        every {
            googleProvider.extractText(any())
        } returns """
            Milk
            2L
        """.trimIndent()

        every {
            parser.parseShoppingList(any())
        } returns expected

        val result =
            service.extractShoppingListWithGoogle(
                byteArrayOf(1, 2, 3)
            )

        assertEquals(
            expected,
            result
        )
    }
}