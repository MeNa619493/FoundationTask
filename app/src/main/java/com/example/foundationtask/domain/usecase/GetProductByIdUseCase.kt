package com.example.foundationtask.domain.usecase

import com.example.foundationtask.domain.models.ProductDomainModel
import com.example.foundationtask.domain.repository.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Int): ProductDomainModel? {
        val product = productRepository.getProductById(id) ?: return null

        val favoriteState = productRepository
            .getProducts()
            ?.firstOrNull { it.id == product.id }
            ?.isFavorite
            ?: product.isFavorite

        return product.copy(isFavorite = favoriteState)
    }
}