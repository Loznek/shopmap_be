package com.example.DTO

import kotlinx.serialization.Serializable

@Serializable
class SimpleRoutePlanning (
    val storeId: Int,
    val concreteShopList: ConcreteShopList
    )