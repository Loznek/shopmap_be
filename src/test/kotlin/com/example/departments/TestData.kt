package com.example.departments


import AppUser
import com.example.model.entity.Department
import com.example.model.entity.Map
import com.example.model.entity.Product
import com.example.model.entity.ProductPosition
import com.example.model.entity.Till
import com.example.model.entity.WallBlock

object TestData {

    fun map() = Map(
        id = 1,
        storeId = 1,
        width = 500.0,
        height = 300.0,
        entranceX = 250.0,
        entranceY = 0.0,
        exitX = 250.0,
        exitY = 300.0
    )

    fun till() = Till(
        id = 1,
        mapId = 1,
        startX = 100.0,
        startY = 250.0,
        width = 300.0,
        height = 20.0
    )

    fun department()= Department(
            id = 1,
            mapId = 1,
            name = "Bakery",
            startX = 100.0,
            startY = 100.0,
            width = 100.0,
            height = 50.0
        )

    fun wallBlock() = WallBlock(
        id = 1,
        mapId = 1,
        startX = 0.0,
        startY = 0.0,
        width = 100.0,
        height = 100.0
    )

    fun product() = Product(
        articleNo = 1,
        name = "Milk",
        size = "1L",
        departmentId = 1,
        position = ProductPosition.TOP,
        storeId = 1,
        price = 10.0
    )

    fun user() = AppUser(
        id = 1,
        firebaseUid = "firebase-user",
        email = "test@test.com",
        displayName = "Test User"
    )
}