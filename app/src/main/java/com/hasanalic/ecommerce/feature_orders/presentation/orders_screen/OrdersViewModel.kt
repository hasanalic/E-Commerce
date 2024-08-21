package com.hasanalic.ecommerce.feature_orders.presentation.orders_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases
): ViewModel() {

    private var _ordersState = MutableLiveData(OrdersState())
    val ordersState: LiveData<OrdersState> = _ordersState

    fun getOrders(userId: String) {
        _ordersState.value = OrdersState(isLoading = true)
        viewModelScope.launch {
            when(val result = orderUseCases.getOrdersByUserUseCase(userId)) {
                is Result.Error -> handleGetOrdersError(result.error)
                is Result.Success -> {
                    _ordersState.value = OrdersState(
                        orders = result.data
                    )
                }
            }
        }
    }

    private fun handleGetOrdersError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Sipariş bilgileri alınamadı."
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _ordersState.value = OrdersState(dataError = message)
    }
}