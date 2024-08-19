package com.hasanalic.ecommerce.feature_notification.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notifications")
data class NotificationEntity(
    @ColumnInfo(name = "notification_user_id") var userId: String,
    @ColumnInfo(name = "notification_title") var notificationTitle: String,
    @ColumnInfo(name = "notification_content") var notificationContent: String,
    @ColumnInfo(name = "notification_time") var notificationTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var notificationEntityId: Int = 0
}