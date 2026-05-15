package com.example.sales

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.jsoup.Jsoup

class FlyerScraper(
    private val httpClient: HttpClient
) {

    suspend fun extractRawText(url: String): String {
        val html = httpClient.get(url).body<String>()
        val document = Jsoup.parse(html)

        return document
            .select("p.wp-caption-text")
            .text()
    }
}