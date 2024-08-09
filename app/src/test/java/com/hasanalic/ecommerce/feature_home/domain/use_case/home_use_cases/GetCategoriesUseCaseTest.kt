package com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.repository.FakeHomeRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCategoriesUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var homeRepository: HomeRepository
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Before
    fun setup() {
        homeRepository = FakeHomeRepository()
        getCategoriesUseCase = GetCategoriesUseCase(homeRepository)
    }

    @Test
    fun `Get Categories should return success with category list`() = runBlocking {
        val result = getCategoriesUseCase()
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotEmpty()
    }
}