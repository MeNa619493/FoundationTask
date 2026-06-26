package com.example.foundationtask.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProductItemUiModel(
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val id: Int,
    val images: List<String>,
    val price: Double,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val isFavorite: Boolean
)