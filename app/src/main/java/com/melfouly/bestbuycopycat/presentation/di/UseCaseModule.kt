package com.melfouly.bestbuycopycat.presentation.di

import com.melfouly.bestbuycopycat.domain.repository.MealsRepository
import com.melfouly.bestbuycopycat.domain.usecase.MealsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideUseCase(repository: MealsRepository): MealsUseCase {
        return MealsUseCase(repository)
    }
}