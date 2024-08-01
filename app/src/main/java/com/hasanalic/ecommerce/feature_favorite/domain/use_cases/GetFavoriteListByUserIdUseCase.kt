package com.hasanalic.ecommerce.feature_favorite.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_favorite.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteListByUserIdUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(userId: String): Result<List<FavoritesEntity>, DataError.Local> {
        return favoriteRepository.getFavoriteListByUserId(userId)
    }
}