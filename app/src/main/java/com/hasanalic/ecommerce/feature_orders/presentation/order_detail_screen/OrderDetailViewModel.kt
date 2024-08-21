package com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import com.hasanalic.ecommerce.utils.Constants.ORDER_CANCELLED
import com.hasanalic.ecommerce.utils.Constants.ORDER_RETURNED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases
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
            DataError.Local.NOT_FOUND -> "Sipariş bilgisi alınamadı"
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _orderDetailState.value = OrderDetailState(dataError = message)
    }

    fun updateOrderStatusToCanceled(userId: String, orderId: String) {
        _orderDetailState.value = _orderDetailState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val result = orderUseCases.updateOrderStatusUseCase(ORDER_CANCELLED, userId, orderId)
            when(result) {
                is Result.Error -> handleUpdateOrderStatusError(result.error)
                is Result.Success -> _orderDetailState.value = _orderDetailState.value!!.copy(
                    isOrderStatusUpdateSuccessful = true
                )
            }
        }
    }

    fun updateOrderStatusToReturned(userId: String, orderId: String) {
        _orderDetailState.value = _orderDetailState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val result = orderUseCases.updateOrderStatusUseCase(ORDER_RETURNED, userId, orderId)
            when(result) {
                is Result.Error -> handleUpdateOrderStatusError(result.error)
                is Result.Success -> _orderDetailState.value = _orderDetailState.value!!.copy(
                    isOrderStatusUpdateSuccessful = true
                )
            }
        }
    }

    private fun handleUpdateOrderStatusError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "İşlem gerçekleştirilemedi."
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _orderDetailState.value = _orderDetailState.value!!.copy(
            isLoading = false,
            actionError = message
        )
    }
}