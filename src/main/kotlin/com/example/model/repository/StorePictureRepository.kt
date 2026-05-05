package com.example.model.repository

import com.example.model.entity.StorePicture

interface StorePictureRepository {
    suspend fun getByStoreId(storeId: Int): List<StorePicture>
    suspend fun add(picture: StorePicture): StorePicture
}