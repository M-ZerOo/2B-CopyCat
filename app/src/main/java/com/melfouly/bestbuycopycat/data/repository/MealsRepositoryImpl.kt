package com.melfouly.bestbuycopycat.data.repository

import com.melfouly.bestbuycopycat.data.remote.ApiService
import com.melfouly.bestbuycopycat.domain.model.CategoryResponse
import com.melfouly.bestbuycopycat.domain.model.MealResponse
import com.melfouly.bestbuycopycat.domain.repository.MealsRepository
import io.reactivex.rxjava3.core.Single

class MealsRepositoryImpl(private val apiService: ApiService) : MealsRepository {

    override fun getCategoriesFromRemote(): Single<CategoryResponse> = apiService.getCategories()

    override fun getMealsFromRemote(category: String): Single<MealResponse> =
        apiService.getCategoryMeals(category)
}