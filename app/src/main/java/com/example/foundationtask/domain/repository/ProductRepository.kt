package com.example.foundationtask.domain.repository

import com.example.foundationtask.domain.models.ProductDomainModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductsFlow(forceRefresh: Boolean): Flow<List<ProductDomainModel>?>
    fun getProducts(): List<ProductDomainModel>?
    suspend fun getProductById(id: Int):ProductDomainModel?
    suspend fun toggleFavorite(productId: Int)
}