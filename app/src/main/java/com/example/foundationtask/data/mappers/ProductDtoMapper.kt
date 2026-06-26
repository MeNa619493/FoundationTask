package com.example.foundationtask.data.mappers

import com.example.foundationtask.data.remote.models.ProductDto
import com.example.foundationtask.domain.models.ProductDomainModel

fun ProductDto.toDomainModel() = ProductDomainModel(
    brand = brand.orEmpty(),
    category = category.orEmpty(),
    description = description.orEmpty(),
    discountPercentage = discountPercentage ?: 0.0,
    id = id ?: 0,
    images = images.orEmpty(),
    price = price ?: 0.0,
    rating = rating ?: 0.0,
    stock = stock ?: 0,
    thumbnail = thumbnail.orEmpty(),
    title = title.orEmpty(),
    isFavorite = false
)