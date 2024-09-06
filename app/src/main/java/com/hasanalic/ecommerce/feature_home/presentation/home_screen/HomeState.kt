package com.hasanalic.ecommerce.feature_home.presentation.home_screen

import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.model.Product

data class HomeState(
    val isLoading: Boolean = false,
    val scannedProductId: String? = null,
    val userId: String = ANOMIM_USER_ID,
    val productList: MutableList<Product> = mutableListOf(),
    val categoryList: MutableList<Category> = mutableListOf(),
    val shouldUserMoveToAuthActivity: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null
)