package com.hasanalic.ecommerce.feature_checkout.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.data.dto.OrderEntity
import com.hasanalic.ecommerce.data.dto.OrderProductsEntity
import com.hasanalic.ecommerce.data.dto.PaymentEntity
import com.hasanalic.ecommerce.domain.model.Address
import com.hasanalic.ecommerce.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.domain.repository.CheckoutRepository
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.Constants.AT_DOOR
import com.hasanalic.ecommerce.utils.Constants.BANK_OR_CREDIT_CARD
import com.hasanalic.ecommerce.utils.Constants.BKM_EXPRESS
import com.hasanalic.ecommerce.utils.Constants.DATE_FORMAT
import com.hasanalic.ecommerce.utils.Constants.PAYPAL
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.TimeAndDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository
): ViewModel() {

    private var _statusAddressList = MutableLiveData<Resource<List<Address>>>()
    val statusAddressList: LiveData<Resource<List<Address>>>
        get() = _statusAddressList

    private var _statusOrder = MutableLiveData<OrderEntity>()

    private var _statusCards = MutableLiveData<Resource<List<PaymentEntity>>>()
    val statusCards: LiveData<Resource<List<PaymentEntity>>>
        get() = _statusCards

    private var _statusPayment = MutableLiveData<Resource<Boolean>>()
    val statusPayment: LiveData<Resource<Boolean>>
        get() = _statusPayment

    fun getAddressList(userId: String) {
        _statusAddressList.value = Resource.Loading()
        viewModelScope.launch {
            val response = checkoutRepository.getAddressesByUserId(userId)
            _statusAddressList.value = response
        }
    }

    fun setAddress(addressId: String) {
        val tempAddressList = _statusAddressList.value!!.data

        tempAddressList?.let {addresses ->
            for (address in addresses) {
                address.isSelected = address.addressId == addressId
            }
            _statusAddressList.value = Resource.Success(tempAddressList)
        }
    }

    fun setOrderAddressAndUserId(addressId: String, userId: String) {
        val order = OrderEntity(orderUserId = userId, orderAddressId = addressId)
        _statusOrder.value = order
    }

    fun setOrderCargo(cargo: String) {
        val order = _statusOrder.value!!
        order.orderCargo = cargo
        _statusOrder.value = order
    }

    fun getCards(userId: String) {
        _statusCards.value = Resource.Loading()
        viewModelScope.launch {
            val response = checkoutRepository.getCardsByUserId(userId)
            _statusCards.value = response
        }
    }

    fun setOrderTypeAsDoorAndInitialize() {
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.shoppingCartItemId }
        val orderTotal = ShoppingCartList.totalPrice

        val order = _statusOrder.value!!
        order.orderTotal = orderTotal
        order.orderNo = UUID.randomUUID().toString()
        order.orderProductCount = shoppingCartList.size.toString()
        order.orderStatus = Constants.ORDER_RECEIVED
        order.orderPaymentType = AT_DOOR
        order.orderDate = TimeAndDate.getLocalDate(DATE_FORMAT)
        order.orderTime = TimeAndDate.getTime()
        order.orderTimeStamp = System.currentTimeMillis()

        viewModelScope.launch {
            val response = checkoutRepository.insertOrder(order)

            if (response is Resource.Success) {
                val orderId = response.data
                val orderProductsArray = createOrderProductsList(shoppingCartList, order.orderUserId!!, orderId.toString())
                val responseFromOrderProducts = checkoutRepository.insertAllOrderProducts(*orderProductsArray)

                if (responseFromOrderProducts is Resource.Success) {
                    val responseFromShoppingCartItemsDao = checkoutRepository.deleteShoppingCartItemsByProductIds(order.orderUserId!!, productIdList)

                    if (responseFromShoppingCartItemsDao is Resource.Success) {
                        _statusPayment.value = Resource.Success(true)
                    } else {
                        _statusPayment.value = Resource.Error(null,responseFromShoppingCartItemsDao.message?:"hata")
                    }
                } else {
                    _statusPayment.value = Resource.Error(null,responseFromOrderProducts.message?:"hata")
                }
            } else {
                _statusPayment.value = Resource.Error(null,response.message?:"hata")
            }
        }
    }

    fun setOrderTypeAsCardAndInitialize() {
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.shoppingCartItemId }
        val orderTotal = ShoppingCartList.totalPrice

        val order = _statusOrder.value!!
        order.orderTotal = orderTotal
        order.orderNo = UUID.randomUUID().toString()
        order.orderProductCount = shoppingCartList.size.toString()
        order.orderStatus = Constants.ORDER_RECEIVED
        order.orderPaymentType = BANK_OR_CREDIT_CARD
        order.orderDate = TimeAndDate.getLocalDate(DATE_FORMAT)
        order.orderTime = TimeAndDate.getTime()
        order.orderTimeStamp = System.currentTimeMillis()

        viewModelScope.launch {
            val response = checkoutRepository.insertOrder(order)

            if (response is Resource.Success) {
                val orderId = response.data
                val orderProductsArray = createOrderProductsList(shoppingCartList, order.orderUserId!!, orderId.toString())
                val responseFromOrderProducts = checkoutRepository.insertAllOrderProducts(*orderProductsArray)

                if (responseFromOrderProducts is Resource.Success) {
                    val responseFromShoppingCartItemsDao = checkoutRepository.deleteShoppingCartItemsByProductIds(order.orderUserId!!, productIdList)

                    if (responseFromShoppingCartItemsDao is Resource.Success) {
                        _statusPayment.value = Resource.Success(true)
                    } else {
                        _statusPayment.value = Resource.Error(null,responseFromShoppingCartItemsDao.message?:"hata")
                    }
                } else {
                    _statusPayment.value = Resource.Error(null,responseFromOrderProducts.message?:"hata insertAllOrderProducts")
                }
            } else {
                _statusPayment.value = Resource.Error(null,response.message?:"hata insertOrder")
            }
        }
    }

    fun setOrderTypeAsCardAndSaveCardAndInitialize(cardName: String, cardNumber: String) {
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.shoppingCartItemId }
        val orderTotal = ShoppingCartList.totalPrice

        val order = _statusOrder.value!!
        order.orderTotal = orderTotal
        order.orderNo = UUID.randomUUID().toString()
        order.orderProductCount = shoppingCartList.size.toString()
        order.orderStatus = Constants.ORDER_RECEIVED
        order.orderPaymentType = BANK_OR_CREDIT_CARD
        order.orderDate = TimeAndDate.getLocalDate(DATE_FORMAT)
        order.orderTime = TimeAndDate.getTime()
        order.orderTimeStamp = System.currentTimeMillis()

        viewModelScope.launch {
            val responseFromCard = checkoutRepository.insertCard(PaymentEntity(
                cardName,cardNumber,order.orderUserId
            ))

            if (responseFromCard is Resource.Success) {
                val paymentId = responseFromCard.data
                order.orderPaymentId = paymentId.toString()

                val responseFromOrder = checkoutRepository.insertOrder(order)
                if (responseFromOrder is Resource.Success) {
                    val orderId = responseFromOrder.data
                    val orderProductsArray = createOrderProductsList(shoppingCartList, order.orderUserId!!, orderId.toString())
                    val responseFromOrderProducts = checkoutRepository.insertAllOrderProducts(*orderProductsArray)

                    if (responseFromOrderProducts is Resource.Success) {
                        val responseFromShoppingCartItemsDao = checkoutRepository.deleteShoppingCartItemsByProductIds(order.orderUserId!!, productIdList)

                        if (responseFromShoppingCartItemsDao is Resource.Success) {
                            _statusPayment.value = Resource.Success(true)
                        } else {
                            _statusPayment.value = Resource.Error(null,responseFromShoppingCartItemsDao.message?:"hata")
                        }
                    } else {
                        _statusPayment.value = Resource.Error(null,responseFromOrderProducts.message?:"hata insertAllOrderProducts")
                    }
                } else {
                    _statusPayment.value = Resource.Error(null,responseFromOrder.message?:"hata insertOrder")
                }
            } else {
                _statusPayment.value = Resource.Error(null,responseFromCard.message?:"hata insertCard")
            }
        }
    }

    fun setOrderTypeAsPaypalAndInitialize() {
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.shoppingCartItemId }
        val orderTotal = ShoppingCartList.totalPrice

        val order = _statusOrder.value!!
        order.orderTotal = orderTotal
        order.orderNo = UUID.randomUUID().toString()
        order.orderProductCount = shoppingCartList.size.toString()
        order.orderStatus = Constants.ORDER_RECEIVED
        order.orderPaymentType = PAYPAL
        order.orderDate = TimeAndDate.getLocalDate(DATE_FORMAT)
        order.orderTime = TimeAndDate.getTime()
        order.orderTimeStamp = System.currentTimeMillis()

        viewModelScope.launch {
            val response = checkoutRepository.insertOrder(order)

            if (response is Resource.Success) {
                val orderId = response.data
                val orderProductsArray = createOrderProductsList(shoppingCartList, order.orderUserId!!, orderId.toString())
                val responseFromOrderProducts = checkoutRepository.insertAllOrderProducts(*orderProductsArray)

                if (responseFromOrderProducts is Resource.Success) {
                    val responseFromShoppingCartItemsDao = checkoutRepository.deleteShoppingCartItemsByProductIds(order.orderUserId!!, productIdList)

                    if (responseFromShoppingCartItemsDao is Resource.Success) {
                        _statusPayment.value = Resource.Success(true)
                    } else {
                        _statusPayment.value = Resource.Error(null,responseFromShoppingCartItemsDao.message?:"hata")
                    }
                } else {
                    _statusPayment.value = Resource.Error(null,responseFromOrderProducts.message?:"hata insertAllOrderProducts")
                }
            } else {
                _statusPayment.value = Resource.Error(null,response.message?:"hata insertOrder")
            }
        }
    }

    fun setOrderTypeAsBkmAndInitialize() {
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.shoppingCartItemId }
        val orderTotal = ShoppingCartList.totalPrice

        val order = _statusOrder.value!!
        order.orderTotal = orderTotal
        order.orderNo = UUID.randomUUID().toString()
        order.orderProductCount = shoppingCartList.size.toString()
        order.orderStatus = Constants.ORDER_RECEIVED
        order.orderPaymentType = BKM_EXPRESS
        order.orderDate = TimeAndDate.getLocalDate(DATE_FORMAT)
        order.orderTime = TimeAndDate.getTime()
        order.orderTimeStamp = System.currentTimeMillis()

        viewModelScope.launch {
            val response = checkoutRepository.insertOrder(order)

            if (response is Resource.Success) {
                val orderId = response.data
                val orderProductsArray = createOrderProductsList(shoppingCartList, order.orderUserId!!, orderId.toString())
                val responseFromOrderProducts = checkoutRepository.insertAllOrderProducts(*orderProductsArray)

                if (responseFromOrderProducts is Resource.Success) {
                    val responseFromShoppingCartItemsDao = checkoutRepository.deleteShoppingCartItemsByProductIds(order.orderUserId!!, productIdList)

                    if (responseFromShoppingCartItemsDao is Resource.Success) {
                        _statusPayment.value = Resource.Success(true)
                    } else {
                        _statusPayment.value = Resource.Error(null,responseFromShoppingCartItemsDao.message?:"hata")
                    }
                } else {
                    _statusPayment.value = Resource.Error(null,responseFromOrderProducts.message?:"hata insertAllOrderProducts")
                }
            } else {
                _statusPayment.value = Resource.Error(null,response.message?:"hata insertOrder")
            }
        }
    }

    fun setOrderTypeAsCardByPaymentIdAndInitialize(paymentId: String) {
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.shoppingCartItemId }
        val orderTotal = ShoppingCartList.totalPrice

        val order = _statusOrder.value!!
        order.orderTotal = orderTotal
        order.orderNo = UUID.randomUUID().toString()
        order.orderProductCount = shoppingCartList.size.toString()
        order.orderStatus = Constants.ORDER_RECEIVED
        order.orderPaymentType = BANK_OR_CREDIT_CARD
        order.orderDate = TimeAndDate.getLocalDate(DATE_FORMAT)
        order.orderTime = TimeAndDate.getTime()
        order.orderTimeStamp = System.currentTimeMillis()
        order.orderPaymentId = paymentId

        viewModelScope.launch {
            val response = checkoutRepository.insertOrder(order)

            if (response is Resource.Success) {
                val orderId = response.data
                val orderProductsArray = createOrderProductsList(shoppingCartList, order.orderUserId!!, orderId.toString())
                val responseFromOrderProducts = checkoutRepository.insertAllOrderProducts(*orderProductsArray)

                if (responseFromOrderProducts is Resource.Success) {
                    val responseFromShoppingCartItemsDao = checkoutRepository.deleteShoppingCartItemsByProductIds(order.orderUserId!!, productIdList)

                    if (responseFromShoppingCartItemsDao is Resource.Success) {
                        _statusPayment.value = Resource.Success(true)
                    } else {
                        _statusPayment.value = Resource.Error(null,responseFromShoppingCartItemsDao.message?:"hata")
                    }
                } else {
                    _statusPayment.value = Resource.Error(null,responseFromOrderProducts.message?:"hata insertAllOrderProducts")
                }
            } else {
                _statusPayment.value = Resource.Error(null,response.message?:"hata insertOrder")
            }
        }
    }

    private fun createOrderProductsList(shoppingCartList: List<ShoppingCartItem>, userId: String, orderId: String): Array<OrderProductsEntity> {
        val orderProductsList = mutableListOf<OrderProductsEntity>()
        for (item in shoppingCartList) {
            orderProductsList.add(
                OrderProductsEntity(
                    orderProductsUserId = userId,
                    orderProductsOrderId = orderId,
                    orderProductsProductId = item.shoppingCartItemId,
                    orderProductsProductQuantity = item.shoppingCartItemQuantity.toString(),
                    orderProductsProductImage = item.shoppingCartItemPhoto
                )
            )
        }
        return orderProductsList.toTypedArray()
    }
}