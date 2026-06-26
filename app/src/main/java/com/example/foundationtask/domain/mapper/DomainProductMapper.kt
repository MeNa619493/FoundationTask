package com.example.foundationtask.domain.mapper

import com.example.foundationtask.domain.models.ProductDomainModel
import com.example.foundationtask.ui.model.ProductItemUiModel
import kotlinx.collections.immutable.toPersistentList

fun ProductDomainModel.toUiModel() = ProductItemUiModel(
    brand = brand,
    category = category,
    description = description,
    discountPercentage = discountPercentage,
    id = id,
    images = images.toPersistentList(),
    price = price,
    rating = rating,
    stock = stock,
    thumbnail = thumbnail,
    title = title,
    isFavorite = isFavorite
)