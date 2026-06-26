package com.example.foundationtask.data.cache.datasource

import com.example.foundationtask.domain.models.ProductDomainModel
import kotlinx.coroutines.flow.Flow

interface ProductCacheDataSource {
    suspend fun put(products: List<ProductDomainModel>)
    fun getProducts(): List<ProductDomainModel>?
    fun getProductsFlow(): Flow<List<ProductDomainModel>?>
    suspend fun setFavorite(productId: Int)
    suspend fun resetCache()
}