package com.hasanalic.ecommerce.feature_filter.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.data.FakeFilterRepository
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetBrandsUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var filterRepository: FilterRepository
    private lateinit var getBrandsUseCase: GetBrandsUseCase

    @Before
    fun setup() {
        filterRepository = FakeFilterRepository()
        getBrandsUseCase = GetBrandsUseCase(filterRepository)
    }

    @Test
    fun `Get Brands should return success with brand list`() = runBlocking {
        val result = getBrandsUseCase()
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotEmpty()
    }
}