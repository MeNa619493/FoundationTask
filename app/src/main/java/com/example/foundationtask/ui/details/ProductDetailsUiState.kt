package com.example.foundationtask.ui.details

import com.example.foundationtask.domain.exception.DomainException
import com.example.foundationtask.ui.model.ProductItemUiModel

sealed interface ProductDetailsUiState {
    data object Loading : ProductDetailsUiState

    data class Success(
        val product: ProductItemUiModel
    ) : ProductDetailsUiState

    data class Failure(
        val productId: Int,
        val exception: DomainException
    ) : ProductDetailsUiState
}
