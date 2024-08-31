package com.hasanalic.ecommerce.feature_home.presentation.favorite_screen

import com.hasanalic.ecommerce.feature_home.domain.model.FavoriteProduct

data class FavoriteState(
    val favoriteProductList: MutableList<FavoriteProduct> = mutableListOf(),
    val isLoading: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null,
    val isUserLoggedIn: Boolean = true,
    val userId: String? = null
)