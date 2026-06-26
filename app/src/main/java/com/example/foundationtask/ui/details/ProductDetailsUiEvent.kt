package com.example.foundationtask.ui.details

sealed interface ProductDetailsUiEvent {
    data class LoadProductById(val productId: Int) : ProductDetailsUiEvent
    object OnClickBackIcon : ProductDetailsUiEvent
    data class OnFavoriteClicked(val productId: Int) : ProductDetailsUiEvent
}