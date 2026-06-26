package com.example.foundationtask.domain.models

data class ProductDomainModel(
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