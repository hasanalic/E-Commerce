package com.hasanalic.ecommerce.feature_notification.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.NotificationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor (
    private val notificationUseCases: NotificationUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
): ViewModel() {

    private var _notificationState = MutableLiveData(NotificationState())
    val notificationState: LiveData<NotificationState> = _notificationState

    fun getNotificationsIfUserLoggedIn() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        if (userId == null) {
            _notificationState.value = NotificationState(
                isUserLoggedIn = false
            )
            return
        }
        getUserNotifications(userId)
    }

    private fun getUserNotifications(userId: String) {
        _notificationState.value = NotificationState(isLoading = true)

        viewModelScope.launch {
            when(val result = notificationUseCases.getUserNotificationsUseCase(userId)) {
                is Result.Error -> handleGetUserNotificationError(result.error)
                is Result.Success -> _notificationState.value = NotificationState(notificationList = result.data)
            }
        }
    }

    private fun handleGetUserNotificationError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Notifications could not be received."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _notificationState.value = NotificationState(dataError = message)
    }
}