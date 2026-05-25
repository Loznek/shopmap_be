package com.example.di.modules.infrastructure


import com.example.ocr.parser.ShoppingListParser
import com.example.ocr.providers.GoogleDocumentAiProvider
import com.example.ocr.providers.TesseractOcrProvider
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf

val ocrModule = module {

    singleOf(::TesseractOcrProvider)

    singleOf(::GoogleDocumentAiProvider)

    singleOf(::ShoppingListParser)
}