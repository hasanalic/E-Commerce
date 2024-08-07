package com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteIdByUserIdAndProductIdUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(userId: String, productId: String): Result<Int, DataError.Local> {
        return favoriteRepository.getFavoriteIdByUserIdAndProductId(userId, productId)
    }
}