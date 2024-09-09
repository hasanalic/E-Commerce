package com.hasanalic.ecommerce.feature_checkout.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.DateFormatConstants.DATE_FORMAT
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_RECEIVED
import com.hasanalic.ecommerce.core.presentation.utils.PaymentConstants.AT_DOOR
import com.hasanalic.ecommerce.core.presentation.utils.PaymentConstants.BANK_OR_CREDIT_CARD
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertAllOrderProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import com.hasanalic.ecommerce.core.presentation.utils.TimeAndDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases,
    private val insertAllOrderProductsUseCase: InsertAllOrderProductsUseCase,
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _checkoutState = MutableLiveData(CheckoutState())
    val checkoutState: LiveData<CheckoutState> = _checkoutState

    fun getUserIdAndSetOrderAddressId(addressId: String) {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
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

    fun buyOrderWithCard() {
        executeInsertOrder(BANK_OR_CREDIT_CARD)
    }

    fun buyOrderWithSavedCard(cardId: String) {
        executeInsertOrder(BANK_OR_CREDIT_CARD, cardId)
    }

    fun buyOrderAtDoor() {
        executeInsertOrder(AT_DOOR)
    }

    private fun executeInsertOrder(paymentType: String, cardId: String? = null) {
        _checkoutState.value = _checkoutState.value!!.copy(
            isLoading = true
        )

        viewModelScope.launch {
            val orderEntity = createOrderEntity(paymentType, cardId)
            val result = orderUseCases.insertOrderUseCase(orderEntity)
            handleOrderResult(result)
        }
    }

    private suspend fun handleOrderResult(result: Result<Long, DataError.Local>) {
        when(result) {
            is Result.Error -> handleError(result.error)
            is Result.Success -> {
                val orderId = result.data.toString()
                val shoppingCartList = ShoppingCartList.shoppingCartList

                val orderProducsArray = createOrderProductsList(shoppingCartList, orderId)
                val productIdList = shoppingCartList.map { it.productId }

                insertOrderProductsAndCallDeleteShoppingCartItems(orderProducsArray, productIdList)
            }
        }
    }

    private suspend fun insertOrderProductsAndCallDeleteShoppingCartItems(
        orderProductsArray: Array<OrderProductsEntity>,
        productIdList: List<String>
    ) {
        when(val result = insertAllOrderProductsUseCase(*orderProductsArray)) {
            is Result.Error -> handleError(result.error)
            is Result.Success -> deleteShoppingCartItemsAndFinishPaymentProcess(productIdList)
        }
    }

    private suspend fun deleteShoppingCartItemsAndFinishPaymentProcess(productIdList: List<String>) {
        val orderUserId = _checkoutState.value!!.userId!!
        val result = shoppingCartUseCases
            .deleteShoppingCartItemEntitiesByProductIdListUseCase(orderUserId, productIdList)

        when(result) {
            is Result.Error -> handleError(result.error)
            is Result.Success -> _checkoutState.value = _checkoutState.value!!.copy(
                isLoading = false,
                isPaymentSuccessful = true
            )
        }
    }

    private fun createOrderProductsList(
        shoppingCartList: List<ShoppingCartItem>,
        orderId: String
    ): Array<OrderProductsEntity> {
        return shoppingCartList.map {
            OrderProductsEntity(
                orderProductsUserId = _checkoutState.value!!.userId!!,
                orderProductsOrderId = orderId,
                orderProductsProductId = it.productId,
                orderProductsProductQuantity = it.quantity.toString(),
                orderProductsProductImage = it.photo
            )
        }.toTypedArray()
    }

    private fun handleError(error: DataError.Local) {
        val message = when (error) {
            DataError.Local.DELETION_FAILED -> "Payment transaction could not be completed."
            DataError.Local.INSERTION_FAILED -> "Payment transaction could not be completed."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _checkoutState.value = _checkoutState.value!!.copy(
            isLoading = false,
            dataError = message
        )
    }

    private fun createOrderEntity(paymentType: String, cardId: String?): OrderEntity {
        val orderUserId = _checkoutState.value!!.userId!!
        val orderCargo = _checkoutState.value!!.cargo!!
        val orderAddressId = _checkoutState.value!!.addressId!!

        val orderTotal = ShoppingCartList.totalPrice
        val productCount = ShoppingCartList.shoppingCartList.size.toString()

        return OrderEntity(
            orderUserId = orderUserId,
            orderTotal = orderTotal,
            orderProductCount = productCount,
            orderDate = TimeAndDate.getLocalDate(DATE_FORMAT),
            orderStatus = ORDER_RECEIVED,
            orderNo = UUID.randomUUID().toString(),
            orderCargo = orderCargo,
            orderAddressId = orderAddressId,
            orderCardId = cardId,
            orderPaymentType = paymentType,
            orderTimeStamp = System.currentTimeMillis(),
            orderTime = TimeAndDate.getTime()
        )
    }
}