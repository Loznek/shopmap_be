package com.example.model.repository

import com.example.db.mapping.StoreDAO
import com.example.db.mapping.StoreTable
import com.example.db.mapping.daoToModel
import com.example.db.mapping.suspendTransaction
import com.example.model.entity.Store
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll

class PostgresStoreRepository: StoreRepository {

    override suspend fun update(store: Store): Store =
        suspendTransaction {

            val dao = StoreDAO[store.id!!]

            dao.name = store.name
            dao.location = store.location.toString()

            daoToModel(dao)
        }

    override suspend fun storeById(id: Int): Store? = suspendTransaction {
        StoreDAO.find{(StoreTable.id eq id)}.map(::daoToModel).firstOrNull()
    }

    override suspend fun addStore(store: Store) : Store = suspendTransaction {
        val newStore = StoreDAO.new {
            name = store.name
           location = store.location.toString()
        }
        daoToModel(newStore)
    }

    override suspend fun removeStore(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = StoreTable.deleteWhere {
            StoreTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun getAll(): List<Store>  = suspendTransaction {
        StoreDAO
            .all()
            .map(::daoToModel)
    }
}