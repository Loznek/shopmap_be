package com.example.model.repository

import com.example.model.entity.Product
import com.example.db.mapping.ProductDAO
import com.example.db.mapping.ProductTable
import com.example.db.mapping.daoToModel
import com.example.db.mapping.suspendTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresProductRepository : ProductRepository {

    override suspend fun productById(articleNo: Int): Product? =
        transaction {
            ProductDAO
                .findById(articleNo)
                ?.let { daoToModel(it) }
        }

    override suspend fun productsByStoreId(storeId: Int): List<Product> =
        transaction {
            ProductDAO
                .find { ProductTable.storeId eq storeId }
                .map { daoToModel(it) }
        }

    override suspend fun addProduct(product: Product): Product =
        transaction {
            val dao = ProductDAO.new {
                name = product.name
                size = product.size
                departmentId = product.departmentId
                position = product.position
                storeId = product.storeId
                price = product.price
            }

            daoToModel(dao)
        }

    override suspend fun updateProduct(product: Product): Product =
        suspendTransaction {

            val dao = ProductDAO.findById(product.articleNo!!)
                ?: throw IllegalArgumentException(
                    "Product with articleNo ${product.articleNo} not found"
                )

            dao.name = product.name
            dao.size = product.size
            dao.departmentId = product.departmentId
            dao.position = product.position
            dao.storeId = product.storeId
            dao.price = product.price

            daoToModel(dao)
        }

    override suspend fun removeProductById(articleNo: Int) {
        transaction {
            ProductDAO.findById(articleNo)?.delete()
        }
    }
}