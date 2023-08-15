package com.melfouly.bestbuycopycat.domain.model

data class CategoryState(
    var success: CategoryResponse? = null,
    var isLoading: Boolean = false,
    var error: String? = null
)
