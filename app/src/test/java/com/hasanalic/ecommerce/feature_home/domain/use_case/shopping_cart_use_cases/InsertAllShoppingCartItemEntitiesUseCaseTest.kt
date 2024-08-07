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
class InsertAllShoppingCartItemEntitiesUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var insertAllShoppingCartItemEntitiesUseCase: InsertAllShoppingCartItemEntitiesUseCase

    @Before
    fun setup() {
        shoppingCartRepository = FakeShoppingCartRepository()
        insertAllShoppingCartItemEntitiesUseCase = InsertAllShoppingCartItemEntitiesUseCase(shoppingCartRepository)
    }

    @Test
    fun `Insert All Shopping Cart Entities should return success when insertion is successful`() = runBlocking {
        val shoppingCartItemEntityList = listOf(
            ShoppingCartItemsEntity("1","1",1)
        ).toTypedArray()

        val result = insertAllShoppingCartItemEntitiesUseCase(*shoppingCartItemEntityList)
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(Unit)
    }
}