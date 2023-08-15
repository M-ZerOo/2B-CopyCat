package com.melfouly.bestbuycopycat.domain.usecase

import com.melfouly.bestbuycopycat.domain.repository.MealsRepository

class MealsUseCase(private val repository: MealsRepository) {

    fun getCategories() = repository.getCategoriesFromRemote()

    fun getMeals(category: String) = repository.getMealsFromRemote(category)
}