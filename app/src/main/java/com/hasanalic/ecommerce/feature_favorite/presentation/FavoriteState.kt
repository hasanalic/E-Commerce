package com.hasanalic.ecommerce.feature_favorite.presentation

import com.hasanalic.ecommerce.feature_favorite.domain.model.FavoriteProduct

data class FavoriteState(
    val favoriteProductList: MutableList<FavoriteProduct> = mutableListOf(),
    val isLoading: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null
)