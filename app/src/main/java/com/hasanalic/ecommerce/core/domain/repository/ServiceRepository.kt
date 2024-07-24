package com.hasanalic.ecommerce.core.domain.repository

import com.hasanalic.ecommerce.feature_notification.data.entity.NotificationEntity
import com.hasanalic.ecommerce.utils.Resource

interface ServiceRepository {
    fun getShoppingCartCount(userId: String): Int

    suspend fun getNotifications(): Resource<List<NotificationEntity>>

    suspend fun insertNotification(notificationEntity: NotificationEntity): Resource<Boolean>
}