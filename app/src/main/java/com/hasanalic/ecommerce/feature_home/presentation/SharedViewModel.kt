package com.hasanalic.ecommerce.feature_home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    /*
    private var _stateComparedProductList = MutableLiveData<MutableList<ComparedProduct>>()
    val stateComparedProductList: LiveData<MutableList<ComparedProduct>>
        get() = _stateComparedProductList
     */

    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> = _cartItemCount

    fun updateCartItemCount(count: Int) {
        _cartItemCount.value = count
    }
}