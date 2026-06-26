package com.example.foundationtask.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ProductRoute {

    @Serializable
    data object ProductList : ProductRoute

    @Serializable
    data class ProductDetails(val id: Int) : ProductRoute
}