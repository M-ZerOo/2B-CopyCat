package com.melfouly.bestbuycopycat.data.remote

import com.melfouly.bestbuycopycat.domain.model.CategoryResponse
import com.melfouly.bestbuycopycat.domain.model.MealResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("categories.php")
    fun getCategories(): Single<CategoryResponse>

    @GET("filter.php")
    fun getCategoryMeals(@Query("c") category: String): Single<MealResponse>
}