package com.hasanalic.ecommerce.feature_checkout.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.data.local.CardDao
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImp @Inject constructor(
    private val cardDao: CardDao
) : CardRepository {
    override suspend fun insertCardEntity(cardEntity: CardEntity): Result<String, DataError.Local> {
        return try {
            val result = cardDao.insertCard(cardEntity)
            result?.let {
                Result.Success(it.toString())
            }?: Result.Error(DataError.Local.INSERTION_FAILED)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getCardsByUserId(userId: String): Result<List<CardEntity>, DataError.Local> {
        return try {
            val result = cardDao.getCardsByUserId(userId)
            result?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getCardByUserIdAndCardId(
        userId: String,
        cardId: String
    ): Result<CardEntity, DataError.Local> {
        return try {
            val result = cardDao.getCardByUserIdAndCardId(userId, cardId)
            result?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}