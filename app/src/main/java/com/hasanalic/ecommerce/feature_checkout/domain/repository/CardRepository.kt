package com.hasanalic.ecommerce.feature_checkout.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity

interface CardRepository {
    suspend fun insertCardEntity(cardEntity: CardEntity): Result<String, DataError.Local>

    suspend fun getCardsByUserId(userId: String): Result<List<CardEntity>, DataError.Local>

    suspend fun getCardByUserIdAndCardId(userId: String, cardId: String): Result<CardEntity, DataError.Local>
}