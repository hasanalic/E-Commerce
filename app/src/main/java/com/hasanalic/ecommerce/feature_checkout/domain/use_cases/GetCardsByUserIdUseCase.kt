package com.hasanalic.ecommerce.feature_checkout.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository
import javax.inject.Inject

class GetCardsByUserIdUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(userId: String): Result<List<CardEntity>, DataError.Local> {
        return cardRepository.getCardsByUserId(userId)
    }
}