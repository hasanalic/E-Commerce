package com.hasanalic.ecommerce.feature_order.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_order.data.repository.FakeOrderRepository
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.InsertOrderUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertOrderUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var orderRepository: OrderRepository
    private lateinit var insertOrderUseCase: InsertOrderUseCase

    @Before
    fun setup() {
        orderRepository = FakeOrderRepository()
        insertOrderUseCase = InsertOrderUseCase(orderRepository)
    }

    @Test
    fun `Insert Order returns success when successful`() = runBlocking {
        val orderEntity = OrderEntity("","","","","","","","","","",1L,"")
        val result = insertOrderUseCase(orderEntity)

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}