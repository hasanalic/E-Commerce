package com.hasanalic.ecommerce.feature_order.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_order.data.repository.FakeOrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrderDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetOrderDetailUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var orderRepository: OrderRepository
    private lateinit var getOrderDetailUseCase: GetOrderDetailUseCase

    @Before
    fun setup() {
        orderRepository = FakeOrderRepository()
        getOrderDetailUseCase = GetOrderDetailUseCase(orderRepository)
    }

    @Test
    fun `Get Order Detail returns success with OrderDetail when found`() = runBlocking {
        val result = getOrderDetailUseCase("1")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isInstanceOf(OrderDetail::class.java)
    }
}