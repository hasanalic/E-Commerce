package com.hasanalic.ecommerce.feature_favorite.domain.use_cases

data class FavoriteUseCases (
    val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    val getFavoriteIdByUserIdAndProductIdUseCase: GetFavoriteIdByUserIdAndProductIdUseCase,
    val getFavoriteListByUserIdUseCase: GetFavoriteListByUserIdUseCase,
    val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    val insertFavoriteAndGetIdUseCase: InsertFavoriteAndGetIdUseCase
)