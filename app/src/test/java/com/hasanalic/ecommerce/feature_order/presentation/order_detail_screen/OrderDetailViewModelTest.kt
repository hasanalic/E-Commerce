package com.hasanalic.ecommerce.feature_order.presentation.order_detail_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_order.data.repository.FakeOrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrderDetailUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrdersByUserUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.InsertOrderUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.UpdateOrderStatusUseCase
import com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen.OrderDetailViewModel
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var orderRepository: OrderRepository
    private lateinit var orderUseCases: OrderUseCases
    private lateinit var orderDetailViewModel: OrderDetailViewModel

    @Before
    fun setup() {
        orderRepository = FakeOrderRepository()
        orderUseCases = OrderUseCases(
            getOrderDetailUseCase = GetOrderDetailUseCase(orderRepository),
            getOrdersByUserUseCase = GetOrdersByUserUseCase(orderRepository),
            insertOrderUseCase = InsertOrderUseCase(orderRepository),
            updateOrderStatusUseCase = UpdateOrderStatusUseCase(orderRepository)
        )
        orderDetailViewModel = OrderDetailViewModel(orderUseCases)
    }

    @Test
    fun `getOrderDetail should returns order detail successfuly`() {
        orderDetailViewModel.getOrderDetail("1")
        val state = orderDetailViewModel.orderDetailState.getOrAwaitValue()

        assertThat(state.orderDetail).isNotNull()
        assertThat(state.orderDetail).isInstanceOf(OrderDetail::class.java)

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isOrderStatusUpdateSuccessful).isFalse()
    }

    @Test
    fun `updateOrderStatusToCanceled should updates order status successfuly`() {
        orderDetailViewModel.getOrderDetail("1")
        orderDetailViewModel.updateOrderStatusToCanceled("1","1")
        val state = orderDetailViewModel.orderDetailState.getOrAwaitValue()

        assertThat(state.orderDetail).isNotNull()
        assertThat(state.orderDetail).isInstanceOf(OrderDetail::class.java)

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isOrderStatusUpdateSuccessful).isTrue()
    }

    @Test
    fun `updateOrderStatusToReturned should updates order status successfuly`() {
        orderDetailViewModel.getOrderDetail("1")
        orderDetailViewModel.updateOrderStatusToReturned("1","1")
        val state = orderDetailViewModel.orderDetailState.getOrAwaitValue()

        assertThat(state.orderDetail).isNotNull()
        assertThat(state.orderDetail).isInstanceOf(OrderDetail::class.java)

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isOrderStatusUpdateSuccessful).isTrue()
    }
}