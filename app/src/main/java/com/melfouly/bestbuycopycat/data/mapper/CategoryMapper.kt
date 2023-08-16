package com.melfouly.bestbuycopycat.data.mapper

import com.melfouly.bestbuycopycat.domain.model.Category
import com.melfouly.bestbuycopycat.domain.model.Product

fun Category.toProduct(): Product {
    return Product(
        id = this.idCategory,
        name = this.strCategory,
        rate = "5.0",
        price = "EGP100.00",
        image = this.strCategoryThumb
    )
}