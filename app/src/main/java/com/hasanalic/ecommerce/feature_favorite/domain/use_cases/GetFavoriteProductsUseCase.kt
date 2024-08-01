package com.hasanalic.ecommerce.feature_favorite.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_favorite.domain.model.FavoriteProduct
import com.hasanalic.ecommerce.feature_favorite.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(userId: String): Result<List<FavoriteProduct>, DataError.Local> {
        return favoriteRepository.getFavoriteProducts(userId)
    }
}