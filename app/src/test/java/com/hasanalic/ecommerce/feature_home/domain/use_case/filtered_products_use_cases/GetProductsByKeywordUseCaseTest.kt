package com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFilteredProductsRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FilteredProductsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetProductsByKeywordUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var filteredProductsRepository: FilteredProductsRepository
    private lateinit var getProductsByKeywordUseCase: GetProductsByKeywordUseCase

    @Before
    fun setup() {
        filteredProductsRepository = FakeFilteredProductsRepository()
        getProductsByKeywordUseCase = GetProductsByKeywordUseCase(filteredProductsRepository)
    }

    @Test
    fun `Get Products By Keyword should gets product list by keyword successfuly`() = runBlocking {
        val result = getProductsByKeywordUseCase("1","keyword")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotEmpty()
    }
}