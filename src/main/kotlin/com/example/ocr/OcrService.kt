package com.example.ocr

import com.example.DTO.ShopList
import com.example.ocr.parser.ShoppingListParser
import com.example.ocr.providers.GoogleDocumentAiProvider
import com.example.ocr.providers.TesseractOcrProvider
import com.example.shoppingList.dto.CreateShoppingListItemRequest
import java.io.File

class OcrService(
    private val tesseractProvider: TesseractOcrProvider,
    private val googleProvider: GoogleDocumentAiProvider,
    private val shoppingListParser: ShoppingListParser
) {

    fun extractTextWithTesseract(file: File): String {
        return tesseractProvider.extractText(file)
    }

    fun extractShoppingListWithGoogle(fileBytes: ByteArray): List<CreateShoppingListItemRequest> {
        val text = googleProvider.extractText(fileBytes)
        val items = shoppingListParser.parseShoppingList(text)
        return items
    }
}