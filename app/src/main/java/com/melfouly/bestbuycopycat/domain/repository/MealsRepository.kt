package com.melfouly.bestbuycopycat.domain.repository

import com.melfouly.bestbuycopycat.domain.model.CategoryResponse
import com.melfouly.bestbuycopycat.domain.model.MealResponse
import io.reactivex.rxjava3.core.Single

interface MealsRepository {

    fun getCategoriesFromRemote(): Single<CategoryResponse>

    fun getMealsFromRemote(category: String): Single<MealResponse>
}