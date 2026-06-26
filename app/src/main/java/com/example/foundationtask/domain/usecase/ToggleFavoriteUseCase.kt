package com.example.foundationtask.domain.usecase

import com.example.foundationtask.domain.repository.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ToggleFavoriteUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int) = productRepository.toggleFavorite(productId)
}