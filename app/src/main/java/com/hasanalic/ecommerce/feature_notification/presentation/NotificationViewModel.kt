package com.hasanalic.ecommerce.feature_notification.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.NotificationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor (
    private val notificationUseCases: NotificationUseCases
): ViewModel() {

    private var _notificationState = MutableLiveData(NotificationState())
    val notificationState: LiveData<NotificationState> = _notificationState

    fun getUserNotifications(userId: String) {
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
            DataError.Local.NOT_FOUND -> "Bildirimler alınamadı."
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _notificationState.value = NotificationState(dataError = message)
    }
}