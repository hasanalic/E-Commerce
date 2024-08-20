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

    /*
    private var _statusOrderList = MutableLiveData<Resource<List<Order>>>()
    val statusOrderList: LiveData<Resource<List<Order>>>
        get() = _statusOrderList

    private var _statusAddress = MutableLiveData<Resource<AddressEntity>>()
    val statusAddress: LiveData<Resource<AddressEntity>>
        get() = _statusAddress

    private var _statusPayment = MutableLiveData<Resource<PaymentEntity>>()
    val statusPayment: LiveData<Resource<PaymentEntity>>
        get() = _statusPayment

    private var _statusUpdateOrderStatus = MutableLiveData<Resource<String>>()
    val statusUpdateOrderStatus: LiveData<Resource<String>>
        get() = _statusUpdateOrderStatus


    fun getAddressDetail(userId: String, addressId: String) {
        _statusAddress.value = Resource.Loading()
        viewModelScope.launch{
            val response = repository.getAddressByUserIdAndAddressId(userId, addressId)
            _statusAddress.value = response
        }
    }

    fun getPaymentDetail(userId: String, paymentId: String) {
        _statusPayment.value = Resource.Loading()
        viewModelScope.launch {
            val response = repository.getCardByUserId(userId, paymentId)
            _statusPayment.value = response
        }
    }

    fun updateOrderStatusToCancel(userId: String, orderId: String) {
        _statusUpdateOrderStatus.value = Resource.Loading()
        viewModelScope.launch {
            val response = repository.updateOrderStatus(ORDER_CANCELLED,userId, orderId)
            if (response is Resource.Success) {
                _statusUpdateOrderStatus.value = Resource.Success("Siparişiniz iptal edilmiştir")
            } else {
                _statusUpdateOrderStatus.value = Resource.Error(null,response.message!!)
            }
        }
    }

    fun updateOrderStatusToReturn(userId: String, orderId: String) {
        _statusUpdateOrderStatus.value = Resource.Loading()
        viewModelScope.launch {
            val response = repository.updateOrderStatus(ORDER_RETURN,userId, orderId)
            if (response is Resource.Success) {
                _statusUpdateOrderStatus.value = Resource.Success("Siparişiniz iade edilmiştir")
            } else {
                _statusUpdateOrderStatus.value = Resource.Error(null,response.message!!)
            }
        }
    }

    fun resetUpdateOrderStatus() {
        _statusUpdateOrderStatus = MutableLiveData<Resource<String>>()
    }

    fun resetAddressDetail() {
        _statusAddress = MutableLiveData<Resource<AddressEntity>>()
    }

    fun resetPaymentDetail() {
        _statusPayment = MutableLiveData<Resource<PaymentEntity>>()
    }

     */
}