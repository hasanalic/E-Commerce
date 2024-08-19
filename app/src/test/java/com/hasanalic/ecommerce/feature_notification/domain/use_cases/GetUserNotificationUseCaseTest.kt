package com.hasanalic.ecommerce.feature_notification.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_notification.data.repository.FakeNotificationRepository
import com.hasanalic.ecommerce.feature_notification.domain.repository.NotificationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserNotificationUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var getUserNotificationsUseCase: GetUserNotificationsUseCase

    @Before
    fun setup() {
        notificationRepository = FakeNotificationRepository()
        getUserNotificationsUseCase = GetUserNotificationsUseCase(notificationRepository)
    }

    @Test
    fun `Get User Notifications successfuly returns notification list`() = runBlocking {
        val result = getUserNotificationsUseCase("1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotEmpty()
    }
}