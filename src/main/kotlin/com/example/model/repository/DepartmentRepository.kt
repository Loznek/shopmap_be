package com.example.model.repository

import com.example.model.entity.Department

interface DepartmentRepository {
    suspend fun departmentsByMap(mapId:Int): List<Department>
    suspend fun addDepartment(department: Department): Department
    suspend fun removeDepartmentById(id: Int): Boolean
    suspend fun updateDepartment(department: Department): Department

}