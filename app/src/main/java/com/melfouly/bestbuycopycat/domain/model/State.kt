package com.melfouly.bestbuycopycat.domain.model

data class State(
    var success: List<Product>? = null,
    var isLoading: Boolean = false,
    var error: String? = null
)
