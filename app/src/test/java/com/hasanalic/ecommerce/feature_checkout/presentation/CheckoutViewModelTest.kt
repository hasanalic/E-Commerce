package com.hasanalic.ecommerce.feature_checkout.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.GetUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.IsDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.LogOutUserUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SaveUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SetDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_checkout.data.repository.FakeOrderProductsRepository
import com.hasanalic.ecommerce.feature_checkout.domain.repository.OrderProductsRepository
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertAllOrderProductsUseCase
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.CheckIfProductInCartUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.DeleteShoppingCartItemEntitiesByProductIdListUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.DeleteShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.GetProductsInShoppingCartUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.GetShoppingCartItemCountUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.InsertAllShoppingCartItemEntitiesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.InsertShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.UpdateShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_order.data.repository.FakeOrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrderDetailUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrdersByUserUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.InsertOrderUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.UpdateOrderStatusUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckoutViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val shoppingCartItemList = listOf(ShoppingCartItem("1","category","photo","brand","detail",10,0,1))
    private val totalPrice = "10"

    private lateinit var orderRepository: OrderRepository
    private lateinit var orderProductsRepository: OrderProductsRepository
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var orderUseCases: OrderUseCases
    private lateinit var insertAllOrderProductsUseCase: InsertAllOrderProductsUseCase
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var checkoutViewModel: CheckoutViewModel

    @Before
    fun setup() {
        orderRepository = FakeOrderRepository()
        orderProductsRepository = FakeOrderProductsRepository()
        shoppingCartRepository = FakeShoppingCartRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        orderUseCases = OrderUseCases(
            getOrderDetailUseCase = GetOrderDetailUseCase(orderRepository),
            getOrdersByUserUseCase = GetOrdersByUserUseCase(orderRepository),
            insertOrderUseCase = InsertOrderUseCase(orderRepository),
            updateOrderStatusUseCase = UpdateOrderStatusUseCase(orderRepository)
        )

        insertAllOrderProductsUseCase = InsertAllOrderProductsUseCase(orderProductsRepository)

        shoppingCartUseCases = ShoppingCartUseCases(
            checkIfProductInCartUseCase = CheckIfProductInCartUseCase(shoppingCartRepository),
            deleteShoppingCartItemEntitiesByProductIdListUseCase = DeleteShoppingCartItemEntitiesByProductIdListUseCase(shoppingCartRepository),
            deleteShoppingCartItemEntityUseCase = DeleteShoppingCartItemEntityUseCase(shoppingCartRepository),
            getProductsInShoppingCartUseCase = GetProductsInShoppingCartUseCase(shoppingCartRepository),
            getShoppingCartItemCountUseCase = GetShoppingCartItemCountUseCase(shoppingCartRepository),
            insertAllShoppingCartItemEntitiesUseCase = InsertAllShoppingCartItemEntitiesUseCase(shoppingCartRepository),
            insertShoppingCartItemEntityUseCase = InsertShoppingCartItemEntityUseCase(shoppingCartRepository),
            updateShoppingCartItemEntityUseCase = UpdateShoppingCartItemEntityUseCase(shoppingCartRepository)
        )

        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        ShoppingCartList.shoppingCartList = shoppingCartItemList
        ShoppingCartList.totalPrice = totalPrice

        sharedPreferencesUseCases.saveUserIdUseCase("1")
        checkoutViewModel = CheckoutViewModel(orderUseCases, insertAllOrderProductsUseCase, shoppingCartUseCases, sharedPreferencesUseCases)
    }

    @Test
    fun `setOrderUserIdAndAddressId should set user id and address id successfuly`() {
        val addressId = "1"

        checkoutViewModel.getUserIdAndSetOrderAddressId(addressId)
        val state = checkoutViewModel.checkoutState.getOrAwaitValue()

        assertThat(state.userId).isEqualTo("1")
        assertThat(state.addressId).isEqualTo(addressId)
        assertThat(state.cargo).isNull()

        assertThat(state.isPaymentSuccessful).isFalse()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `setOrderCargo should set user id and address id successfuly`() {
        val addressId = "1"
        val cargo = "cargo"

        checkoutViewModel.getUserIdAndSetOrderAddressId(addressId)
        checkoutViewModel.setOrderCargo(cargo)
        val state = checkoutViewModel.checkoutState.getOrAwaitValue()

        assertThat(state.userId).isEqualTo("1")
        assertThat(state.addressId).isEqualTo(addressId)
        assertThat(state.cargo).isEqualTo(cargo)

        assertThat(state.isPaymentSuccessful).isFalse()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `buyOrderWithCard should successfuly complete payment process`() {
        val addressId = "1"
        val cargo = "cargo"

        checkoutViewModel.getUserIdAndSetOrderAddressId(addressId)
        checkoutViewModel.setOrderCargo(cargo)

        checkoutViewModel.buyOrderWithCard()

        val state = checkoutViewModel.checkoutState.getOrAwaitValue()

        assertThat(state.userId).isEqualTo("1")
        assertThat(state.addressId).isEqualTo(addressId)
        assertThat(state.cargo).isEqualTo(cargo)

        assertThat(state.isPaymentSuccessful).isTrue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `buyOrderWithSavedCard should successfuly complete payment process`() {
        val addressId = "1"
        val cargo = "cargo"
        val cardId = "1"

        checkoutViewModel.getUserIdAndSetOrderAddressId(addressId)
        checkoutViewModel.setOrderCargo(cargo)

        checkoutViewModel.buyOrderWithSavedCard(cardId)

        val state = checkoutViewModel.checkoutState.getOrAwaitValue()

        assertThat(state.userId).isEqualTo("1")
        assertThat(state.addressId).isEqualTo(addressId)
        assertThat(state.cargo).isEqualTo(cargo)

        assertThat(state.isPaymentSuccessful).isTrue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `buyOrderAtDoor should successfuly complete payment process`() {
        val addressId = "1"
        val cargo = "cargo"

        checkoutViewModel.getUserIdAndSetOrderAddressId(addressId)
        checkoutViewModel.setOrderCargo(cargo)

        checkoutViewModel.buyOrderAtDoor()

        val state = checkoutViewModel.checkoutState.getOrAwaitValue()

        assertThat(state.userId).isEqualTo("1")
        assertThat(state.addressId).isEqualTo(addressId)
        assertThat(state.cargo).isEqualTo(cargo)

        assertThat(state.isPaymentSuccessful).isTrue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }
}