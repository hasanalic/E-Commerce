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

class GetReviewsProductIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var productDetailRepository: ProductDetailRepository
    private lateinit var getReviewsByProductIdUseCase: GetReviewsByProductIdUseCase

    @Before
    fun setup() {
        productDetailRepository = FakeProductDetailRepository()
        getReviewsByProductIdUseCase = GetReviewsByProductIdUseCase(productDetailRepository)
    }

    @Test
    fun `Get Reviews By Product Id should return success`() = runBlocking {
        val result = getReviewsByProductIdUseCase("1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotNull()
    }
}