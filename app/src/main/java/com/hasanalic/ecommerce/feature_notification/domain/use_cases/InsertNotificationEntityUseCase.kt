package com.hasanalic.ecommerce.feature_notification.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity
import com.hasanalic.ecommerce.feature_notification.domain.repository.NotificationRepository
import javax.inject.Inject

class InsertNotificationEntityUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationEntity: NotificationEntity): Result<Unit, DataError.Local> {
        return notificationRepository.insertNotificationEntity(notificationEntity)
    }
}