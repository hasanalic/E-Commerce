package com.hasanalic.ecommerce.feature_notification.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity

@Dao
interface NotificationDao {
    @Query("SELECT * FROM Notifications ORDER BY notification_time DESC")
    suspend fun getNotifications(): List<NotificationEntity>?

    @Insert
    suspend fun insertNotification(notificationEntity: NotificationEntity): Long
}