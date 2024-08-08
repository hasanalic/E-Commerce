package com.hasanalic.ecommerce.feature_filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {

    private var _stateCategoryList = MutableLiveData<List<Category>>()
    val stateCategoryList: LiveData<List<Category>>
        get() = _stateCategoryList

    private var _stateBrandList = MutableLiveData<List<Brand>>()
    val stateBrandList: LiveData<List<Brand>>
        get() = _stateBrandList

    fun selectCategory(category: String) {
        _stateCategoryList.value?.let {
            for (chip in it) {
                if (chip.category == category) {
                    chip.isSelected = true
                    getBrandsByCategory(category)
                } else {
                    chip.isSelected = false
                }
            }
        }
    }

    fun selectBrand(brand: String) {
        _stateBrandList.value?.let {
            for (chip in it) {
                if (chip.brand == brand) {
                    chip.isSelected = true
                } else {
                    chip.isSelected = false
                }
            }
        }
    }

    fun getCategoryAndBrandList() {
        getCategories()
        getBrands()
    }

    private fun getCategories() {
        /*
        viewModelScope.launch {
            val response = homeRepository.getCategories()
            if (response is Resource.Success) {
                _stateCategoryList.value = response.data!!
            } else if (response is Resource.Error) {
                _stateCategoryList.value = emptyList()
            }
        }

         */
    }

    private fun getBrands() {
        /*
        viewModelScope.launch {
            val response = homeRepository.getBrands()
            if (response is Resource.Success) {
                _stateBrandList.value = response.data!!
            } else {
                _stateBrandList.value = emptyList()
            }
        }

         */
    }

    private fun getBrandsByCategory(category: String) {
        /*
        viewModelScope.launch {
            val response = homeRepository.getBrandsByCategory(category)
            if (response is Resource.Success) {
                _stateBrandList.value = response.data!!
            } else {
                _stateBrandList.value = emptyList()
            }
        }

         */
    }
}