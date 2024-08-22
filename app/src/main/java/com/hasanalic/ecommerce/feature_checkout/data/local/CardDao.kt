package com.hasanalic.ecommerce.feature_checkout.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(cardEntity: CardEntity): Long?

    @Query("SELECT * FROM Card WHERE card_user_id = :userId")
    suspend fun getCardsByUserId(userId: String): List<CardEntity>?

    @Query("SELECT * FROM Card WHERE card_user_id = :userId AND cardId = :cardId")
    suspend fun getCardByUserIdAndCardId(userId: String, cardId: String): CardEntity?
}