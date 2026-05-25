package com.example.di.modules

import PostgresUserRepository
import UserRepository

import com.example.model.repository.*

import com.example.repository.PostgresShoppingListItemRepository
import com.example.repository.PostgresShoppingListRepository
import com.example.repository.ShoppingListItemRepository
import com.example.repository.ShoppingListRepository

import org.koin.dsl.module

val repositoryModule = module {

    single<DepartmentRepository> {
        PostgresDepartmentRepository()
    }

    single<MapRepository> {
        PostgresMapRepository()
    }

    single<StoreRepository> {
        PostgresStoreRepository()
    }

    single<WallBlockRepository> {
        PostgresWallBlockRepository()
    }

    single<TillRepository> {
        PostgresTillRepository()
    }

    single<ProductRepository> {
        PostgresProductRepository()
    }

    single<GoogleMapsInfoRepository> {
        PostgresGoogleMapsInfoRepository()
    }

    single<OpeningHoursRepository> {
        PostgresOpeningHoursRepository()
    }

    single<StorePictureRepository> {
        PostgresStorePictureRepository()
    }

    single<UserRepository> {
        PostgresUserRepository()
    }

    single<ShoppingListRepository> {
        PostgresShoppingListRepository()
    }

    single<ShoppingListItemRepository> {
        PostgresShoppingListItemRepository()
    }
}