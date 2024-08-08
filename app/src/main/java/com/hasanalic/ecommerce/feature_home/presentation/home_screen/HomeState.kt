package com.hasanalic.ecommerce.feature_home.presentation.home_screen

import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.model.Product

data class HomeState(
    val isLoading: Boolean = false,
    val productList: MutableList<Product> = mutableListOf(),
    val categoryList: MutableList<Category> = mutableListOf(),
    val isCompareButtonVisible: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null
)