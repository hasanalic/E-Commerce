package com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertShoppingCartItemEntityUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var insertShoppingCartItemEntityUseCase: InsertShoppingCartItemEntityUseCase

    @Before
    fun setup() {
        shoppingCartRepository = FakeShoppingCartRepository()
        insertShoppingCartItemEntityUseCase = InsertShoppingCartItemEntityUseCase(shoppingCartRepository)
    }

    @Test
    fun `Insert Shopping Cart Item Entity should return success when insertion is successful`() = runBlocking {
        val result = insertShoppingCartItemEntityUseCase(ShoppingCartItemsEntity("1","1",1))
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(Unit)
    }
}