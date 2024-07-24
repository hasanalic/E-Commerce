package com.hasanalic.ecommerce.feature_auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.domain.repository.CheckoutRepository
import com.hasanalic.ecommerce.domain.repository.HomeRepository
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val checkoutRepository: CheckoutRepository
): ViewModel() {

    private var _stateShoppingCartItems = MutableLiveData<Resource<Boolean>>()
    val stateShoppingCartItems: LiveData<Resource<Boolean>>
        get() = _stateShoppingCartItems

    fun updateUsersShoppingCartEntities(userId: String, anomimUserId: String) {
        _stateShoppingCartItems.value = Resource.Loading()
        viewModelScope.launch {
            val responseFromAnomimShoppingCartEntities = homeRepository.getShoppingCartItems(anomimUserId)

            if (responseFromAnomimShoppingCartEntities is Resource.Success) {
                val responseFromUsersShoppingCartEntities = homeRepository.getShoppingCartItems(userId)

                if (responseFromUsersShoppingCartEntities is Resource.Success) {

                    val anomimShoppingCartEntityList = responseFromAnomimShoppingCartEntities.data
                    val userShoppingCartEntityList = responseFromUsersShoppingCartEntities.data
                    var willBeAddedAnomimShoppingCartEntityList = anomimShoppingCartEntityList

                    if (userShoppingCartEntityList!!.isNotEmpty()) {
                        if (anomimShoppingCartEntityList!!.isNotEmpty()) {
                            for (userShoppingCartEntity in userShoppingCartEntityList) {
                                willBeAddedAnomimShoppingCartEntityList = willBeAddedAnomimShoppingCartEntityList!!.filter {willBeAddedShoppingCartEntity ->
                                    willBeAddedShoppingCartEntity.productId != userShoppingCartEntity.productId
                                }
                            }
                        }
                    }

                    if (anomimShoppingCartEntityList!!.isNotEmpty()) {
                        val productIdList = anomimShoppingCartEntityList.map { it.productId!! }
                        val responseDeleteShoppingCartItems = checkoutRepository.deleteShoppingCartItemsByProductIds(anomimUserId, productIdList)

                        if (responseDeleteShoppingCartItems is Resource.Success) {
                            if (willBeAddedAnomimShoppingCartEntityList!!.isNotEmpty()) {
                                willBeAddedAnomimShoppingCartEntityList.forEach {
                                    it.userId = userId
                                }
                                val responseInsertAllShoppingCartItems = homeRepository.insertAllShoppingCartItems(*willBeAddedAnomimShoppingCartEntityList.toTypedArray())

                                if (responseInsertAllShoppingCartItems is Resource.Success) {
                                    _stateShoppingCartItems.value = Resource.Success(true)
                                } else {
                                    _stateShoppingCartItems.value = Resource.Error(null,responseInsertAllShoppingCartItems.message?:"responseInsertAllShoppingCartItems")
                                }
                            } else {
                                _stateShoppingCartItems.value = Resource.Success(true)
                            }
                        } else {
                            _stateShoppingCartItems.value = Resource.Error(null,responseDeleteShoppingCartItems.message?:"responseDeleteShoppingCartItems")
                        }
                    } else {
                        _stateShoppingCartItems.value = Resource.Success(true)
                    }

                } else {
                    _stateShoppingCartItems.value = Resource.Error(null,responseFromUsersShoppingCartEntities.message?:"responseFromUsersShoppingCartEntities")
                }
            } else {
                _stateShoppingCartItems.value = Resource.Error(null,responseFromAnomimShoppingCartEntities.message?:"responseFromAnomimShoppingCartEntities")
            }
        }
    }
}