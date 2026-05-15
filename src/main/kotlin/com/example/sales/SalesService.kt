package com.example.sales

import com.example.sales.dto.SalesItem

class SalesService(
    private val flyerScraper: FlyerScraper,
    private val productParser: ProductParser
) {

    suspend fun parseSales(url: String): List<SalesItem> {
        val rawText = flyerScraper.extractRawText(url)

        return productParser
            .splitProducts(rawText)
            .map { SalesItem(rawText = it) }
    }
}