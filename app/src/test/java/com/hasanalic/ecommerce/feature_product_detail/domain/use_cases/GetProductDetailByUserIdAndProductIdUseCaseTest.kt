package com.hasanalic.ecommerce.feature_product_detail.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_product_detail.data.repository.FakeProductDetailRepository
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetProductDetailByUserIdAndProductIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var productDetailRepository: ProductDetailRepository
    private lateinit var getProductDetailByUserIdAndProductIdUseCase: GetProductDetailByUserIdAndProductIdUseCase

    @Before
    fun setup() {
        productDetailRepository = FakeProductDetailRepository()
        getProductDetailByUserIdAndProductIdUseCase = GetProductDetailByUserIdAndProductIdUseCase(productDetailRepository)
    }

    @Test
    fun `Get Product Detail should return success when product id in db`() = runBlocking {
        val result = getProductDetailByUserIdAndProductIdUseCase("1","1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotNull()
    }

    @Test
    fun `Get Product Detail should return error NOT FOUND when user id or product id not found`() = runBlocking {
        val result = getProductDetailByUserIdAndProductIdUseCase("1","invalid")
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }
}