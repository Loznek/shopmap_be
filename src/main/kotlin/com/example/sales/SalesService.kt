package com.example.sales

import com.example.sales.dto.SalesItem
import com.example.sales.dto.SalesResponse

class SalesService(
    private val flyerScraper: FlyerScraper,
    private val productParser: ProductParser
) {

    suspend fun getSales(storeName: String): SalesResponse {
        return flyerScraper.getSales(storeName)


        /*
        val rawText = flyerScraper.extractRawText(url)

        return productParser
            .splitProducts(rawText)
            .map { SalesItem(rawText = it) }


         */
    }
}