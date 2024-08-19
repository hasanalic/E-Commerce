package com.hasanalic.ecommerce.feature_notification.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity
import com.hasanalic.ecommerce.feature_notification.domain.repository.NotificationRepository

class FakeNotificationRepository: NotificationRepository {
    private val notificationList = listOf(NotificationEntity("1","title","content",1L))

    override suspend fun getUserNotifications(userId: String): Result<List<NotificationEntity>, DataError.Local> {
        return Result.Success(notificationList)
    }

    override suspend fun insertNotificationEntity(notificationEntity: NotificationEntity): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }
}