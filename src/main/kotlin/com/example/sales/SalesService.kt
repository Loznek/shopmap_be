package com.example.sales

import com.example.sales.dto.SalesItem
import com.example.sales.dto.SalesResponse

class SalesService(
    private val flyerScraper: FlyerScraper,

) {
    suspend fun getSales(storeName: String): SalesResponse {
        return flyerScraper.getSales(storeName)
    }
}