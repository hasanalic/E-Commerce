package com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.bouncycastle.util.test.FixedSecureRandom.Data
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetProductsInShoppingCartUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var getProductsInShoppingCartUseCase: GetProductsInShoppingCartUseCase

    @Before
    fun setup() {
        shoppingCartRepository = FakeShoppingCartRepository()
        getProductsInShoppingCartUseCase = GetProductsInShoppingCartUseCase(shoppingCartRepository)
    }

    @Test
    fun `Get Products In Shopping Cart should return success with shopping cart item list when user id exists in db`() = runBlocking {
        val result = getProductsInShoppingCartUseCase("1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotEmpty()
    }

    @Test
    fun `Get Products In Shopping Cart should return error NOT FOUND when there is no user id in db`() = runBlocking {
        val result = getProductsInShoppingCartUseCase("2")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(DataError.Local.NOT_FOUND)
    }
}