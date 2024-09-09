package com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_CANCELLED
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_RETURNED
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _orderDetailState = MutableLiveData(OrderDetailState())
    val orderDetailState: LiveData<OrderDetailState> = _orderDetailState

    fun getOrderDetail(orderId: String) {
        _orderDetailState.value = OrderDetailState(isLoading = true)
        viewModelScope.launch {
            when(val result = orderUseCases.getOrderDetailUseCase(orderId)) {
                is Result.Error -> handleGetOrderDetailError(result.error)
                is Result.Success -> _orderDetailState.value = OrderDetailState(orderDetail = result.data)
            }
        }
    }

    private fun handleGetOrderDetailError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Order information could not be obtained."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _orderDetailState.value = OrderDetailState(dataError = message)
    }

    fun updateOrderStatusToCanceled() {
        _orderDetailState.value = _orderDetailState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val orderId = _orderDetailState.value!!.orderDetail!!.orderId
            val userId = sharedPreferencesUseCases.getUserIdUseCase() ?: ANOMIM_USER_ID
            val result = orderUseCases.updateOrderStatusUseCase(ORDER_CANCELLED, userId, orderId)
            when(result) {
                is Result.Error -> handleUpdateOrderStatusError(result.error)
                is Result.Success -> _orderDetailState.value = _orderDetailState.value!!.copy(
                    isOrderStatusUpdateSuccessful = true,
                    isLoading = false
                )
            }
        }
    }

    fun updateOrderStatusToReturned() {
        _orderDetailState.value = _orderDetailState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val orderId = _orderDetailState.value!!.orderDetail!!.orderId
            val userId = sharedPreferencesUseCases.getUserIdUseCase() ?: ANOMIM_USER_ID
            val result = orderUseCases.updateOrderStatusUseCase(ORDER_RETURNED, userId, orderId)
            when(result) {
                is Result.Error -> handleUpdateOrderStatusError(result.error)
                is Result.Success -> _orderDetailState.value = _orderDetailState.value!!.copy(
                    isOrderStatusUpdateSuccessful = true,
                    isLoading = false
                )
            }
        }
    }

    private fun handleUpdateOrderStatusError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "The operation could not be performed."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _orderDetailState.value = _orderDetailState.value!!.copy(
            isLoading = false,
            actionError = message
        )
    }
}