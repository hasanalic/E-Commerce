package com.hasanalic.ecommerce.feature_notification.domain.use_cases

data class NotificationUseCases(
    val getUserNotificationsUseCase: GetUserNotificationsUseCase,
    val insertNotificationEntityUseCase: InsertNotificationEntityUseCase
)