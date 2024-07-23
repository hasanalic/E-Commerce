package com.hasanalic.ecommerce.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.data.dto.NotificationEntity

@Dao
interface NotificationDao {
    @Query("SELECT * FROM Notifications ORDER BY notification_time DESC")
    suspend fun getNotifications(): List<NotificationEntity>?

    @Insert
    suspend fun insertNotification(notificationEntity: NotificationEntity): Long
}