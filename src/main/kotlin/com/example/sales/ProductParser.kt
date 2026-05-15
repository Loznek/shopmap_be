package com.example.sales

class ProductParser {

    fun splitProducts(text: String): List<String> {
        val cleaned = text.replace(Regex("\\s+"), " ").trim()

        val regex = Regex("(?=\\s[A-ZÁÉÍÓÖŐÚÜŰ]{3,},)")

        return cleaned
            .split(regex)
            .map { it.trim() }
            .filter { it.contains("Ft") }
    }
}