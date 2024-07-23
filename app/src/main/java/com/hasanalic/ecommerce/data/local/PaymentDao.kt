package com.hasanalic.ecommerce.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.data.dto.PaymentEntity

@Dao
interface PaymentDao {
    @Insert
    suspend fun insertCard(paymentEntity: PaymentEntity): Long?

    @Query("SELECT * FROM Payment WHERE payment_user_id = :userId")
    suspend fun getCardsByUserId(userId: String): List<PaymentEntity>?

    @Query("SELECT * FROM Payment WHERE payment_user_id = :userId AND paymentId = :paymentId")
    suspend fun getCardByUserId(userId: String, paymentId: String): PaymentEntity?
}