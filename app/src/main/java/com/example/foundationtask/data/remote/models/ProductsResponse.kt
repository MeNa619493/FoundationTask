package com.example.foundationtask.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    val limit: Int? = null,
    val products: List<ProductDto>? = null,
    val skip: Int? = null,
    val total: Int? = null
)

@Serializable
data class ProductDto(
    val brand: String? = null,
    val category: String? = null,
    val description: String? = null,
    val discountPercentage: Double? = null,
    val id: Int? = null,
    val images: List<String>? = null,
    val price: Double? = null,
    val rating: Double? = null,
    val stock: Int? = null,
    val thumbnail: String? = null,
    val title: String? = null
)