package com.example.foundationtask.ui.list

import androidx.compose.runtime.Stable
import com.example.foundationtask.domain.exception.DomainException
import com.example.foundationtask.ui.model.ProductItemUiModel

sealed interface ProductListUiState {
    data object Loading : ProductListUiState

    @Stable
    data class Success(
        val products: List<ProductItemUiModel>,
        val searchQuery: String? = null
    ) : ProductListUiState

    data class Failure(
        val exception: DomainException? = null
    ) : ProductListUiState
}
