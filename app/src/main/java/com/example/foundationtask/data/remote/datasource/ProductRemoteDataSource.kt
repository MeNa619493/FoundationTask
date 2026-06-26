package com.example.foundationtask.data.remote.datasource

import com.example.foundationtask.data.remote.models.ProductDto

interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getProductById(id: Int): ProductDto?
}