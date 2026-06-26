package com.example.foundationtask.ui.localization

import com.example.foundationtask.domain.exception.DomainException

interface ProductCatalogLocalization {
    val searchPlaceholder: String
    val clearSearch: String
    val noProductsTitle: String
    val noResultsTitle: String
    val emptyMessage: String
    val retry: String
    val addToFavorites: String
    val removeFromFavorites: String
    val back: String
    val ratingLabel: String
    val outOfStock: String
    val descriptionLabel: String
    fun errorTitle(exception: DomainException?): String
    fun errorMessage(exception: DomainException?): String
    fun priceLabel(amount: String): String
    fun itemCount(count: Int): String
    fun noResultsMessage(query: String): String
    fun onlyLeft(count: Int): String
    fun inStock(count: Int): String
}