package com.example.foundationtask.di

import com.example.foundationtask.data.remote.datasource.ProductRemoteDataSource
import com.example.foundationtask.data.remote.datasource.ProductRemoteDataSourceImpl
import com.example.foundationtask.data.remote.service.ProductsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideProductRemoteDataSource(
        apiService: ProductsApiService
    ): ProductRemoteDataSource {
        return ProductRemoteDataSourceImpl(apiService)
    }
}