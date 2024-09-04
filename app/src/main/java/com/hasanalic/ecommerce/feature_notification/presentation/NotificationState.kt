package com.hasanalic.ecommerce.feature_notification.presentation

import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity

data class NotificationState(
    val isLoading: Boolean = false,
    val notificationList: List<NotificationEntity> = emptyList(),
    val isUserLoggedIn: Boolean = true,
    val dataError: String? = null
)