package com.example.products.dto


import com.example.model.entity.Product
import com.example.model.entity.ProductPosition
import com.example.products.dto.CreateProductRequest
import com.example.products.dto.ProductResponse
import com.example.products.dto.UpdateProductRequest

fun CreateProductRequest.toEntity(): Product =
    Product(
        articleNo = null,
        name = name,
        size = size,
        departmentId = departmentId,
        position = position?.let { ProductPosition.valueOf(it) },
        storeId = storeId,
        price = price
    )

fun UpdateProductRequest.toEntity(): Product =
    Product(
        articleNo = articleNo,
        name = name,
        size = size,
        departmentId = departmentId,
        position = position?.let { ProductPosition.valueOf(it) },
        storeId = storeId,
        price = price
    )

fun Product.toResponse(): ProductResponse =
    ProductResponse(
        articleNo = articleNo,
        name = name,
        size = size,
        departmentId = departmentId,
        position = position?.name,
        storeId = storeId,
        price = price
    )