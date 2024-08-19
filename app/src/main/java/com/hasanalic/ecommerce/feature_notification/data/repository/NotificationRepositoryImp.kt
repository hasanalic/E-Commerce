package com.hasanalic.ecommerce.feature_notification.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_notification.data.local.NotificationDao
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity
import com.hasanalic.ecommerce.feature_notification.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    override suspend fun getUserNotifications(userId: String): Result<List<NotificationEntity>, DataError.Local> {
        return try {
            val result = notificationDao.getNotifications(userId)
            result?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertNotificationEntity(notificationEntity: NotificationEntity): Result<Unit, DataError.Local> {
        return try {
            val result = notificationDao.insertNotification(notificationEntity)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}