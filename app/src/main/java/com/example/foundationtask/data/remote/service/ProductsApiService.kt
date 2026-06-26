package com.example.foundationtask.data.remote.service

import com.example.foundationtask.data.remote.models.ProductDto
import com.example.foundationtask.data.remote.models.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsApiService {
    @GET("/products")
    suspend fun getProducts(): ProductsResponse?

    @GET("/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductDto?
}