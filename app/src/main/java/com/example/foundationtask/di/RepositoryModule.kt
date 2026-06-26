package com.example.foundationtask.di

import com.example.foundationtask.data.cache.datasource.ProductCacheDataSource
import com.example.foundationtask.data.remote.datasource.ProductRemoteDataSource
import com.example.foundationtask.data.repository.ProductRepositoryImpl
import com.example.foundationtask.domain.repository.ProductRepository
import com.example.foundationtask.logger.Logger
import com.example.foundationtask.logger.LoggerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return LoggerImpl()
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        cache: ProductCacheDataSource,
        remote: ProductRemoteDataSource,
        logger: Logger
    ): ProductRepository {
        return ProductRepositoryImpl(
            productCache = cache,
            productRemote = remote,
            logger = logger
        )
    }
}