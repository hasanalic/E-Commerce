package com.hasanalic.ecommerce.feature_filter.presentation

import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category

data class FilterState(
    val isLoading: Boolean = false,
    val categoryList: List<Category> = mutableListOf(),
    val brandList: List<Brand> = mutableListOf(),
    val dataError: String? = null,
    val actionError: String? = null
)