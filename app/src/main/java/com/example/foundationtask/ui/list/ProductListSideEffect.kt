package com.example.foundationtask.ui.list

sealed interface ProductListSideEffect {
    data class NavigateToDetails(val id: Int) : ProductListSideEffect
}