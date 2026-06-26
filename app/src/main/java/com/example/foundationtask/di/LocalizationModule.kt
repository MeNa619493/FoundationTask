package com.example.foundationtask.di

import com.example.foundationtask.ui.localization.ProductCatalogLocalization
import com.example.foundationtask.ui.localization.ProductCatalogLocalizationImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface LocalizationModule {

    @Binds
    @Singleton
    fun bindProductCatalogLocalization(
        impl: ProductCatalogLocalizationImpl
    ): ProductCatalogLocalization
}