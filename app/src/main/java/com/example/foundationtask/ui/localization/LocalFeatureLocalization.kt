package com.example.foundationtask.ui.localization

import androidx.compose.runtime.compositionLocalOf

val LocalFeatureLocalization = compositionLocalOf<ProductCatalogLocalization> {
    error("ProductCatalogLocalization not provided")
}