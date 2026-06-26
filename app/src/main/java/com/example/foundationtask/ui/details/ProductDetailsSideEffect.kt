package com.example.foundationtask.ui.details

sealed interface ProductDetailsSideEffect {
    object PopBackStack: ProductDetailsSideEffect
}