package com.example.model.repository

import com.example.db.mapping.DepartmentTable
import com.example.model.entity.Product
import com.example.db.mapping.ProductDAO
import com.example.db.mapping.ProductTable
import com.example.db.mapping.daoToModel
import com.example.db.mapping.suspendTransaction
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresProductRepository : ProductRepository {

    override suspend fun productById(articleNo: Int): Product? =
        suspendTransaction {
            ProductDAO
                .findById(articleNo)
                ?.let { daoToModel(it) }
        }

    override suspend fun productsByStoreId(storeId: Int): List<Product> =
        suspendTransaction {
            ProductDAO
                .find { ProductTable.storeId eq storeId }
                .map { daoToModel(it) }
        }

    override suspend fun addProduct(product: Product): Product =
        suspendTransaction {
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
                ?: throw NotFoundException(
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

    override suspend fun removeProductById(articleNo: Int):Boolean  =
        suspendTransaction {
            val rowsDeleted = DepartmentTable.deleteWhere {
                DepartmentTable.id eq articleNo
            }
             rowsDeleted == 1
        }

}