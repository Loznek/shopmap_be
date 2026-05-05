package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.StorePicture

class PostgresStorePictureRepository : StorePictureRepository {

    override suspend fun getByStoreId(storeId: Int): List<StorePicture> = suspendTransaction {
        StorePictureDAO.find { StorePicturesTable.storeId eq storeId }
            .map(::storePictureDaoToModel)
    }

    override suspend fun add(picture: StorePicture): StorePicture = suspendTransaction {

        val store = StoreDAO.findById(picture.storeId)
            ?: throw Exception("Store not found")

        val new = StorePictureDAO.new {
            this.store = store
            this.path = picture.path
        }

        storePictureDaoToModel(new)
    }
}