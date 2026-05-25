package com.example.di.modules.infrastructure


import com.example.stores.GooglePlacesClient
import com.example.stores.PhotoDownloader
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val GOOGLE_API_KEY = "YOUR_GOOGLE_API_KEY"

val googleMapsModule = module {

    single(named("googleApiKey")) {
        getProperty<String>("google.apiKey")
    }

    single {
        GooglePlacesClient(
            client = get(),
            apiKey = get(named("googleApiKey"))
        )
    }

    single {
        PhotoDownloader(
            client = get(),
            apiKey = get(named("googleApiKey"))
        )
    }
}