package com.hasanalic.ecommerce.feature_orders.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.feature_location.data.entity.AddressEntity
import com.hasanalic.ecommerce.feature_checkout.data.entity.PaymentEntity
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.utils.Constants.ORDER_CANCELLED
import com.hasanalic.ecommerce.utils.Constants.ORDER_RETURN
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
): ViewModel() {

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

    fun getOrderList(userId: String) {
        _statusOrderList.value = Resource.Loading()

        viewModelScope.launch {
            val responseFromOrder = repository.getOrdersByUserId(userId)

            if (responseFromOrder is Resource.Success) {
                var tempOrderList = mutableListOf<Order>()

                responseFromOrder.data?.let {orderEntityList ->
                    for (orderEntity in orderEntityList) {
                        val responseFromOrderProducts = repository.getOrderProductsList(userId, orderEntity.orderId.toString())

                        if (responseFromOrderProducts is Resource.Success) {
                            tempOrderList.add(
                                Order(
                                orderId = orderEntity.orderId.toString(),
                                orderUserId = orderEntity.orderUserId!!,
                                orderTotal = orderEntity.orderTotal!!,
                                orderProductCount = orderEntity.orderProductCount!!,
                                orderDate = orderEntity.orderDate!!,
                                orderStatus = orderEntity.orderStatus!!,
                                orderNo = orderEntity.orderNo!!,
                                orderCargo = orderEntity.orderCargo!!,
                                orderAddressId = orderEntity.orderAddressId!!,
                                orderPaymentId = orderEntity.orderPaymentId,
                                orderPaymentType = orderEntity.orderPaymentType!!,
                                orderTimeStamp = orderEntity.orderTimeStamp!!,
                                orderTime = orderEntity.orderTime!!,
                                orderProductsList = responseFromOrderProducts.data!!
                            )
                            )
                        } else {
                            _statusOrderList.value = Resource.Error(null,responseFromOrderProducts.message?:"hata")
                        }
                    }
                    _statusOrderList.value = Resource.Success(tempOrderList)
                }
            } else {
                _statusOrderList.value = Resource.Error(null,responseFromOrder.message?:"hata")
            }
        }
    }

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
}