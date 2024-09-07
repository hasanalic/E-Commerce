package com.hasanalic.ecommerce.feature_home.presentation.filtered_screen

import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_home.domain.model.Product

data class FilteredProductsState (
    val isLoading: Boolean = false,
    val userId: String = ANOMIM_USER_ID,
    val productList: List<Product> = listOf(),
    val shouldUserMoveToAuthActivity: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null
)