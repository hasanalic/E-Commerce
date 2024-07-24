package com.hasanalic.ecommerce.feature_filter.presentation

data class Filter(
    var category: String? = null,
    var brand: String? = null,
    var minPrice: Int? = null,
    var maxPrice: Int? = null,
    var minStar: Double? = null,
    var maxStar: Double? = null
)
