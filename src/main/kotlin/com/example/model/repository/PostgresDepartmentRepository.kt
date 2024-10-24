package com.example.model.repository

import com.example.db.mapping.*
import com.example.model.entity.Department
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresDepartmentRepository: DepartmentRepository{
    override suspend fun departmentsByMap(mapId: Int): List<Department> = suspendTransaction {
        DepartmentDAO.find{(DepartmentTable.mapId eq mapId)}.map(::daoToModel)
    }

    override suspend fun addDepartment(department: Department) : Unit = suspendTransaction {
        DepartmentDAO.new {
            width = department.width
            height = department.height
            startX = department.startX
            startY = department.startY
            mapId = department.mapId
            name = department.name
        }
    }


    override suspend fun removeDepartmentById(id:Int): Boolean = suspendTransaction {
        val rowsDeleted = DepartmentTable.deleteWhere {
            DepartmentTable.id eq id
        }
        rowsDeleted == 1
    }
}