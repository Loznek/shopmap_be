package com.example.algorithms

import com.example.model.entity.Department
import com.example.model.entity.Till
import com.example.model.entity.WallBlock
import com.example.model.entity.Map

object PositionChecker {

    fun checkNewBlockposition(startX:Double , startY:Double , width:Double , height:Double, wallblocks: List<WallBlock>, departments: List<Department>, tills: List<Till>, map: Map): Boolean {
        if (startX < 0 || startY < 0 || width < 0 || height < 0) {
            return false
        }
        if (startX + width > map.width || startY + height > map.height) {
            return false
        }
        for (block in wallblocks) {
            if (block.startX < startX + width && block.startX + block.width > startX && block.startY < startY + height && block.startY + block.height > startY) {
                return false
            }
        }
        for (department in departments) {
            if (department.startX < startX + width && department.startX + department.width > startX && department.startY < startY + height && department.startY + department.height > startY) {
                return false
            }
        }
        for (till in tills) {
            if (till.startX < startX + width && till.startX + till.width > startX && till.startY < startY + height && till.startY + till.height > startY) {
                return false
            }
        }
        return true
    }

    fun checkNewMapSizes(width: Double, height: Double, wallBlocksByMap: List<WallBlock>, departmentsByMap: List<Department>, tillsByMap: List<Till>, mapById: Map): Boolean {
        if (width < 0 || height < 0) {
            return false
        }
        for (block in wallBlocksByMap) {
            if (block.startX + block.width > width || block.startY + block.height > height) {
                return false
            }
        }
        for (department in departmentsByMap) {
            if (department.startX + department.width > width || department.startY + department.height > height) {
                return false
            }
        }
        for (till in tillsByMap) {
            if (till.startX + till.width > width || till.startY + till.height > height) {
                return false
            }
        }
        return true
    }


}