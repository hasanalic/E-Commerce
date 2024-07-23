package com.hasanalic.ecommerce.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.data.dto.NotificationEntity
import com.hasanalic.ecommerce.domain.repository.ServiceRepository
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