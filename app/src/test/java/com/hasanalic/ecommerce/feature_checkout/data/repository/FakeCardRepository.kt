package com.hasanalic.ecommerce.feature_checkout.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository

class FakeCardRepository: CardRepository {
    private val cardEntity = CardEntity("card name","1234123412341234","1")
    private val cardList = listOf(cardEntity)

    override suspend fun insertCardEntity(cardEntity: CardEntity): Result<String, DataError.Local> {
        return Result.Success("1")
    }

    override suspend fun getCardsByUserId(userId: String): Result<List<CardEntity>, DataError.Local> {
        val filteredCardList = cardList.filter { it.cardUserId == userId }
        return if (filteredCardList.isEmpty()) Result.Error(DataError.Local.NOT_FOUND) else Result.Success(filteredCardList)
    }

    override suspend fun getCardByUserIdAndCardId(
        userId: String,
        cardId: String
    ): Result<CardEntity, DataError.Local> {
        return if (userId == cardEntity.cardUserId) Result.Success(cardEntity) else Result.Error(DataError.Local.NOT_FOUND)
    }
}