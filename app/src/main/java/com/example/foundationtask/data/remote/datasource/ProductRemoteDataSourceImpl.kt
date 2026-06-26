package com.example.foundationtask.data.remote.datasource

import com.example.foundationtask.data.remote.models.ProductDto
import com.example.foundationtask.data.remote.service.ProductsApiService

class ProductRemoteDataSourceImpl(
    private val apiService: ProductsApiService
) : ProductRemoteDataSource {
    override suspend fun getProducts(): List<ProductDto>? =
        apiService.getProducts()?.products

    override suspend fun getProductById(id: Int): ProductDto? =
        apiService.getProductById(id)
}