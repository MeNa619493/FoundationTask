package com.example.foundationtask.ui.list

sealed interface ProductListUiEvent {
    data class LoadProducts(val forceRefresh: Boolean) : ProductListUiEvent
    data class OnProductClicked(val id: Int) : ProductListUiEvent
    data class OnFavoriteClicked(val productId: Int) : ProductListUiEvent
    data class OnQueryChanged(val query: String) : ProductListUiEvent
}