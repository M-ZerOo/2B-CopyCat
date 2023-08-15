package com.melfouly.bestbuycopycat.presentation.di

import com.melfouly.bestbuycopycat.data.remote.ApiService
import com.melfouly.bestbuycopycat.data.repository.MealsRepositoryImpl
import com.melfouly.bestbuycopycat.domain.repository.MealsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepo(apiService: ApiService): MealsRepository {
        return MealsRepositoryImpl(apiService)
    }
}