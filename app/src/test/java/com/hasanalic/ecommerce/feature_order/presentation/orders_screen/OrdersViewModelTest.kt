package com.hasanalic.ecommerce.feature_order.presentation.orders_screen

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
import com.hasanalic.ecommerce.feature_order.data.repository.FakeOrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrderDetailUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrdersByUserUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.InsertOrderUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.UpdateOrderStatusUseCase
import com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.OrdersViewModel
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OrdersViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var orderRepository: OrderRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var orderUseCases: OrderUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var ordersViewModel: OrdersViewModel

    @Before
    fun setup() {
        orderRepository = FakeOrderRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        orderUseCases = OrderUseCases(
            getOrderDetailUseCase = GetOrderDetailUseCase(orderRepository),
            getOrdersByUserUseCase = GetOrdersByUserUseCase(orderRepository),
            insertOrderUseCase = InsertOrderUseCase(orderRepository),
            updateOrderStatusUseCase = UpdateOrderStatusUseCase(orderRepository)
        )
        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        ordersViewModel = OrdersViewModel(orderUseCases, sharedPreferencesUseCases)
    }

    @Test
    fun `getOrders should fetches orders successfuly`() {
        ordersViewModel.getOrders()
        val state = ordersViewModel.ordersState.getOrAwaitValue()

        assertThat(state.orders).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }
}