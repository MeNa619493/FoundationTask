package com.example.foundationtask.domain.usecase

import com.example.foundationtask.domain.models.ProductDomainModel
import com.example.foundationtask.domain.repository.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(
        query: String
    ): List<ProductDomainModel>? = productRepository.getProducts()
        ?.filter { it.title.contains(other = query, ignoreCase = true) }
}