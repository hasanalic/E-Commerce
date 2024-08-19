package com.hasanalic.ecommerce.feature_notification.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity
import com.hasanalic.ecommerce.core.domain.repository.ServiceRepository
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
): ViewModel() {

    private var _stateNotificationList = MutableLiveData<Resource<List<NotificationEntity>>>()
    val stateNotificationList: LiveData<Resource<List<NotificationEntity>>>
        get() = _stateNotificationList

    init {
        getNotificationList()
    }

    private fun getNotificationList() {
        _stateNotificationList.value = Resource.Loading()
        viewModelScope.launch {
            _stateNotificationList.value = serviceRepository.getNotifications()
        }
    }
}