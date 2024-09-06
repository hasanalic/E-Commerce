package com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckIfProductInCartUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var checkIfProductInCartUseCase: CheckIfProductInCartUseCase

    @Before
    fun setup() {
        shoppingCartRepository = FakeShoppingCartRepository()
        checkIfProductInCartUseCase = CheckIfProductInCartUseCase(shoppingCartRepository)
    }

    @Test
    fun `Check If Product In Cart should return success with true when shopping cart entity exists in db`() = runBlocking {
        val result = checkIfProductInCartUseCase("1","1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(true)
    }

    @Test
    fun `Check If Product In Cart should return success with false when there is no shopping cart entity in db`() = runBlocking {
        val result = checkIfProductInCartUseCase("1","2")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(false)
    }
}