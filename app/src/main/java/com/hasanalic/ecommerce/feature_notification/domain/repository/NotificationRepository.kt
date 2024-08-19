package com.hasanalic.ecommerce.feature_notification.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity

interface NotificationRepository {

    suspend fun getUserNotifications(userId: String): Result<List<NotificationEntity>, DataError.Local>

    suspend fun insertNotificationEntity(notificationEntity: NotificationEntity): Result<Unit, DataError.Local>
}