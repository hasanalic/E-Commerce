package com.hasanalic.ecommerce.feature_filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.FilterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterUseCases: FilterUseCases
): ViewModel() {

    private var _filterState = MutableLiveData(FilterState())
    val filterState: LiveData<FilterState> = _filterState

    fun getCategoryAndBrandList() {
        _filterState.value = FilterState(isLoading = true)
        viewModelScope.launch {
            getCategoryList()
        }
    }

    private suspend fun getCategoryList() {
        when(val result = filterUseCases.getCategoriesUseCase()) {
            is Result.Error -> handleGetCategoryListError(result.error)
            is Result.Success -> {
                _filterState.value = FilterState(categoryList = result.data)
                getBrandList()
            }
        }
    }

    private fun handleGetCategoryListError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Could not get category list."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _filterState.value = FilterState(dataError = message)
    }

    private suspend fun getBrandList() {
        when(val result = filterUseCases.getBrandsUseCase()) {
            is Result.Error -> handleGetBrandListError(result.error)
            is Result.Success -> {
                _filterState.value = _filterState.value!!.copy(
                    brandList = result.data
                )
            }
        }
    }

    private fun handleGetBrandListError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Could not get brand list."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _filterState.value = FilterState(dataError = message)
    }

    fun selectCategory(itemIndex: Int, categoryValue: String) {
        val currentCategoryList = _filterState.value!!.categoryList

        for ((index, category) in currentCategoryList.withIndex()) {
            if (index == itemIndex) category.isSelected = true
            else category.isSelected = false
        }

        _filterState.value = _filterState.value!!.copy(
            categoryList = currentCategoryList
        )

        getBrandListByCategory(categoryValue)
    }

    private fun getBrandListByCategory(category: String) {
        viewModelScope.launch {
            when(val result = filterUseCases.getBrandsByCategoryUseCase(category)) {
                is Result.Error -> handleGetBrandListError(result.error)
                is Result.Success -> {
                    _filterState.value = _filterState.value!!.copy(
                        brandList = result.data
                    )
                }
            }
        }
    }

    fun selectBrand(itemIndex: Int) {
        val currentBrandList = _filterState.value!!.brandList

        for ((index, brand) in currentBrandList.withIndex()) {
            if (index == itemIndex) brand.isSelected = true
            else brand.isSelected = false
        }

        _filterState.value = _filterState.value!!.copy(
            brandList = currentBrandList
        )
    }
}