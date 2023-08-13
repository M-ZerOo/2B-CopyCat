package com.melfouly.bestbuycopycat.model

import androidx.annotation.DrawableRes

data class Product(
    val name: String,
    val rate: String,
    val price: String,
    @DrawableRes val image: Int,
    val isFav: Boolean = false
)
