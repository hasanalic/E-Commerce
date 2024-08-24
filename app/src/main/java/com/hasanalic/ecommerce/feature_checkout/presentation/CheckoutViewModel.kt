package com.hasanalic.ecommerce.feature_checkout.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertAllOrderProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases,
    private val insertAllOrderProductsUseCase: InsertAllOrderProductsUseCase,
    private val shoppingCartUseCases: ShoppingCartUseCases
): ViewModel() {

    private var _checkoutState = MutableLiveData(CheckoutState())
    val checkoutState: LiveData<CheckoutState> = _checkoutState

    fun setOrderUserIdAndAddressId(userId: String, addressId: String) {
        _checkoutState.value = _checkoutState.value!!.copy(
            userId = userId,
            addressId = addressId
        )
    }

    fun setOrderCargo(cargo: String) {
        _checkoutState.value = _checkoutState.value!!.copy(
            cargo = cargo
        )
    }

    /*
    private var _statusAddressList = MutableLiveData<Resource<List<Address>>>()
    val statusAddressList: LiveData<Resource<List<Address>>>
        get() = _statusAddressList

    private var _statusOrder = MutableLiveData<OrderEntity>()

    private var _statusCards = MutableLiveData<Resource<List<CardEntity>>>()
    val statusCards: LiveData<Resource<List<CardEntity>>>
        get() = _statusCards

    private var _statusPayment = MutableLiveData<Resource<Boolean>>()
    val statusPayment: LiveData<Resource<Boolean>>
        get() = _statusPayment

    fun setOrderTypeAsDoorAndInitialize() {
        /*
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.productId }
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

         */
    }

    fun setOrderTypeAsCardAndInitialize() {
        /*
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.productId }
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

         */
    }

    fun setOrderTypeAsCardAndSaveCardAndInitialize(cardName: String, cardNumber: String) {
        /*
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.productId }
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
            val responseFromCard = checkoutRepository.insertCard(
                CardEntity(
                cardName,cardNumber,order.orderUserId
            )
            )

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

         */
    }

    fun setOrderTypeAsPaypalAndInitialize() {
        /*
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.productId }
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

         */
    }

    fun setOrderTypeAsBkmAndInitialize() {
        /*
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.productId }
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

         */
    }

    fun setOrderTypeAsCardByPaymentIdAndInitialize(paymentId: String) {
        /*
        _statusPayment.value = Resource.Loading()

        val shoppingCartList = ShoppingCartList.shoppingCartList
        val productIdList = shoppingCartList.map { it.productId }
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

         */
    }

    private fun createOrderProductsList(shoppingCartList: List<ShoppingCartItem>, userId: String, orderId: String): Array<OrderProductsEntity> {
        val orderProductsList = mutableListOf<OrderProductsEntity>()
        for (item in shoppingCartList) {
            orderProductsList.add(
                OrderProductsEntity(
                    orderProductsUserId = userId,
                    orderProductsOrderId = orderId,
                    orderProductsProductId = item.productId,
                    orderProductsProductQuantity = item.quantity.toString(),
                    orderProductsProductImage = item.photo
                )
            )
        }
        return orderProductsList.toTypedArray()
    }

     */
}