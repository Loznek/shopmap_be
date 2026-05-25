package com.example.di.modules.infrastructure



import com.example.sales.FlyerScraper
import com.example.sales.ProductParser
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf

val salesModule = module {

    singleOf(::FlyerScraper)

    singleOf(::ProductParser)
}