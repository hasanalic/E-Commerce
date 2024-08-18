package com.hasanalic.ecommerce.feature_filter.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_filter.data.FakeFilterRepository
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.FilterUseCases
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsByCategoryUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetCategoriesUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FilterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var filterRepository: FilterRepository
    private lateinit var filterUseCases: FilterUseCases
    private lateinit var filterViewModel: FilterViewModel

    @Before
    fun setup() {
        filterRepository = FakeFilterRepository()
        filterUseCases = FilterUseCases(
            getBrandsUseCase = GetBrandsUseCase(filterRepository),
            getCategoriesUseCase = GetCategoriesUseCase(filterRepository),
            getBrandsByCategoryUseCase = GetBrandsByCategoryUseCase(filterRepository)
        )
        filterViewModel = FilterViewModel(filterUseCases)
    }

    @Test
    fun `getCategoryAndBrandList successfult returns category and brand list`() = runBlocking {
        filterViewModel.getCategoryAndBrandList()
        val result = filterViewModel.filterState.getOrAwaitValue()

        assertThat(result.isLoading).isFalse()
        assertThat(result.categoryList).isNotEmpty()
        assertThat(result.brandList).isNotEmpty()
        assertThat(result.dataError).isNull()
        assertThat(result.actionError).isNull()
    }

    @Test
    fun `selectCategory sets isSelected of clicked category true `() {
        filterViewModel.getCategoryAndBrandList()
        filterViewModel.selectCategory(0,"category")

        val result = filterViewModel.filterState.getOrAwaitValue()

        assertThat(result.isLoading).isFalse()
        assertThat(result.categoryList).isNotEmpty()
        assertThat(result.categoryList[0].isSelected).isTrue()
        assertThat(result.categoryList[1].isSelected).isFalse()

        assertThat(result.brandList).isNotEmpty()
        assertThat(result.dataError).isNull()
        assertThat(result.actionError).isNull()
    }

    @Test
    fun `selectBrand sets isSelected of clicked brand true`() {
        filterViewModel.getCategoryAndBrandList()
        filterViewModel.selectBrand(0)

        val result = filterViewModel.filterState.getOrAwaitValue()

        assertThat(result.isLoading).isFalse()
        assertThat(result.categoryList).isNotEmpty()
        assertThat(result.brandList).isNotEmpty()
        assertThat(result.brandList[0].isSelected).isTrue()
        assertThat(result.brandList[1].isSelected).isFalse()

        assertThat(result.dataError).isNull()
        assertThat(result.actionError).isNull()
    }
}