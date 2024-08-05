package com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases

data class FavoriteUseCases (
    val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    val getFavoriteIdByUserIdAndProductIdUseCase: GetFavoriteIdByUserIdAndProductIdUseCase,
    val getFavoriteListByUserIdUseCase: GetFavoriteListByUserIdUseCase,
    val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    val insertFavoriteAndGetIdUseCase: InsertFavoriteAndGetIdUseCase
)