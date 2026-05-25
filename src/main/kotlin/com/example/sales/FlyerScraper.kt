package com.example.sales

import com.example.sales.dto.SalesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.jsoup.Jsoup
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class FlyerScraper(
    private val httpClient: HttpClient
) {

    suspend fun getSales(
        storeName: String
    ): SalesResponse {

        val store =
            StoreChain.valueOf(storeName.uppercase())

        val (start, end) = currentFlyerPeriod()

        val url = buildFlyerUrl(store)

        println(url)

        val html = httpClient.get(url).body<String>()

        val document = Jsoup.parse(html)

        val description =
            document.selectFirst("div.product-description")
                ?: throw IllegalStateException("No product description found")

        val offers =
            description
                .select("ul ul li")
                .map { it.text().trim() }
                .filter { it.isNotBlank() }

        println(offers)

        return SalesResponse(
            store = store.name,
            validFrom = start.toString(),
            validTo = end.toString(),
            offers = offers
        )
    }

    private fun currentFlyerPeriod(): Pair<LocalDate, LocalDate> {

        val today = LocalDate.now()

        val start =
            today.with(
                TemporalAdjusters.nextOrSame(
                    DayOfWeek.THURSDAY
                )
            )

        val end = start.plusDays(6)

        return start to end
    }

    private fun buildFlyerUrl(
        store: StoreChain
    ): String {

        val (start, end) = currentFlyerPeriod()

        val startPart =
            start.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )

        val endPart =
            end.format(
                DateTimeFormatter.ofPattern("MM-dd")
            )

        return "https://akcios-ujsag.hu/akcios-ujsagok/" + "${store.slug}-akcios-ujsag-$startPart-$endPart/"
    }



}