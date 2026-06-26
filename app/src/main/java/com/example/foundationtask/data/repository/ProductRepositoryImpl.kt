package com.example.foundationtask.data.repository

import com.example.foundationtask.data.cache.datasource.ProductCacheDataSource
import com.example.foundationtask.logger.Logger
import com.example.foundationtask.data.mappers.toDomainException
import com.example.foundationtask.data.mappers.toDomainModel
import com.example.foundationtask.data.remote.datasource.ProductRemoteDataSource
import com.example.foundationtask.data.remote.exceptions.ServiceException
import com.example.foundationtask.data.utils.handleRequest
import com.example.foundationtask.domain.models.ProductDomainModel
import com.example.foundationtask.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(
    private val productCache: ProductCacheDataSource,
    private val productRemote: ProductRemoteDataSource,
    private val logger: Logger
) : ProductRepository {
    override fun getProductsFlow(forceRefresh: Boolean): Flow<List<ProductDomainModel>?> = flow {
        if (forceRefresh || productCache.getProducts() == null) {
            updateProductCache()
        }
        emitAll(productCache.getProductsFlow())
    }

    override fun getProducts(): List<ProductDomainModel>? = productCache.getProducts()

    private suspend fun updateProductCache() {
        try {
            val remoteProducts = handleRequest(
                remoteCall = { productRemote.getProducts() },
                onFailure = { throwable -> logger.logError(throwable) }
            )
            val domainModels = remoteProducts?.map { it.toDomainModel() } ?: emptyList()
            if (domainModels.isNotEmpty()) {
                productCache.put(domainModels)
            } else {
                productCache.resetCache()
            }
        } catch (e: ServiceException) {
            throw e.toDomainException()
        }
    }

    override suspend fun getProductById(id: Int): ProductDomainModel? {
        try {
            val remoteProducts = handleRequest(
                remoteCall = { productRemote.getProductById(id) },
                onFailure = { throwable -> logger.logError(throwable) }
            )
            return remoteProducts?.toDomainModel()
        } catch (e: ServiceException) {
            throw e.toDomainException()
        }
    }

    override suspend fun toggleFavorite(productId: Int) {
        productCache.setFavorite(productId = productId)
    }
}
