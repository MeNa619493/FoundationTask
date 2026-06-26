package com.example.foundationtask.di

import com.example.foundationtask.data.cache.datasource.ProductCacheDataSource
import com.example.foundationtask.data.cache.datasource.ProductCacheDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CacheModule {

    @Provides
    @Singleton
    fun provideProductCacheDataSource(): ProductCacheDataSource {
        return ProductCacheDataSourceImpl()
    }
}