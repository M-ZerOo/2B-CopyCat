package com.melfouly.bestbuycopycat.data.mapper

import com.melfouly.bestbuycopycat.domain.model.Meal
import com.melfouly.bestbuycopycat.domain.model.Product

fun Meal.toProduct(): Product {
    return Product(
        id = this.idMeal,
        name = this.strMeal,
        rate = "5.0",
        price = "EGP100.00",
        image = this.strMealThumb
    )
}
