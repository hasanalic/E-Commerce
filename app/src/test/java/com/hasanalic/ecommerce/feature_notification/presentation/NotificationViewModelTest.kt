package com.hasanalic.ecommerce.feature_notification.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.GetUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.IsDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.LogOutUserUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SaveUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SetDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
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
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var notificationUseCases: NotificationUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var notificationViewModel: NotificationViewModel

    @Before
    fun setup() {
        notificationRepository = FakeNotificationRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        notificationUseCases = NotificationUseCases(
            getUserNotificationsUseCase = GetUserNotificationsUseCase(notificationRepository),
            insertNotificationEntityUseCase = InsertNotificationEntityUseCase(notificationRepository)
        )
        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

    }

    @Test
    fun `getNotificationsIfUserLoggedIn successfuly returns notification list when user logged in`() {
        sharedPreferencesUseCases.saveUserIdUseCase("1")
        notificationViewModel = NotificationViewModel(notificationUseCases, sharedPreferencesUseCases)

        notificationViewModel.getNotificationsIfUserLoggedIn()

        val state = notificationViewModel.notificationState.getOrAwaitValue()

        assertThat(state.notificationList).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `getNotificationsIfUserLoggedIn sets isUserLoggedIn to false when user not logged in`() {
        notificationViewModel = NotificationViewModel(notificationUseCases, sharedPreferencesUseCases)

        notificationViewModel.getNotificationsIfUserLoggedIn()

        val state = notificationViewModel.notificationState.getOrAwaitValue()

        assertThat(state.notificationList).isEmpty()
        assertThat(state.isUserLoggedIn).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }
}