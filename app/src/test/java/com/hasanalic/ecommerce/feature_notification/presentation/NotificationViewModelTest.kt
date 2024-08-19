package com.hasanalic.ecommerce.feature_notification.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_notification.data.repository.FakeNotificationRepository
import com.hasanalic.ecommerce.feature_notification.domain.repository.NotificationRepository
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.GetUserNotificationsUseCase
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.InsertNotificationEntityUseCase
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.NotificationUseCases
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotificationViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var notificationUseCases: NotificationUseCases
    private lateinit var notificationViewModel: NotificationViewModel

    @Before
    fun setup() {
        notificationRepository = FakeNotificationRepository()
        notificationUseCases = NotificationUseCases(
            getUserNotificationsUseCase = GetUserNotificationsUseCase(notificationRepository),
            insertNotificationEntityUseCase = InsertNotificationEntityUseCase(notificationRepository)
        )
        notificationViewModel = NotificationViewModel(notificationUseCases)
    }

    @Test
    fun `getUserNotifications successfuly returns notification list`() {
        notificationViewModel.getUserNotifications("1")
        val result = notificationViewModel.notificationState.getOrAwaitValue()

        assertThat(result.notificationList).isNotEmpty()
        assertThat(result.isLoading).isFalse()
        assertThat(result.dataError).isNull()
    }
}