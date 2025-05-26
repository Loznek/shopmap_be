package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.Product
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresProductRepository : ProductRepository {

    override suspend fun productsByStoreId(storeId: Int): List<Product> {
        return suspendTransaction {
            ProductDAO.find { ProductTable.storeId eq storeId }
                .map(::daoToModel)
        }
    }

    override suspend fun addProduct(product: Product): Product {
        return suspendTransaction {
            val newProduct = ProductDAO.new {
                name = product.name
                size = product.size
                shelfId = product.shelfId
                price = product.price
                storeId = product.storeId
            }
            daoToModel(newProduct)
        }
    }

    override suspend fun removeProductById(id: Int): Boolean {
        // Implementation for removing a product by ID from PostgreSQL
        return true // Placeholder
    }

    override suspend fun updateProduct(product: Product): Product {
        // Implementation for updating a product in PostgreSQL
        return product // Placeholder
    }

    override suspend fun productById(id: Int): Product? {
        return suspendTransaction {
            ProductDAO.find { ProductTable.id eq id }
                .map(::daoToModel)
                .firstOrNull()
        }
    }

}