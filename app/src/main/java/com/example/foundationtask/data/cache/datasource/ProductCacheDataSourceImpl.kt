package com.example.foundationtask.data.cache.datasource

import com.example.foundationtask.domain.models.ProductDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductCacheDataSourceImpl : ProductCacheDataSource {
    private val _productsFlow = MutableStateFlow<List<ProductDomainModel>?>(null)

    override suspend fun put(products: List<ProductDomainModel>) {
        _productsFlow.value = products
    }

    override fun getProducts(): List<ProductDomainModel>? = _productsFlow.value

    override fun getProductsFlow(): Flow<List<ProductDomainModel>?> = _productsFlow.asStateFlow()

    override suspend fun setFavorite(productId: Int) {
        _productsFlow.update { currentProducts ->
            currentProducts?.map { product ->
                if (product.id == productId)
                    product.copy(isFavorite = !product.isFavorite)
                else
                    product
            }
        }
    }

    override suspend fun resetCache() {
        _productsFlow.value = null
    }
}