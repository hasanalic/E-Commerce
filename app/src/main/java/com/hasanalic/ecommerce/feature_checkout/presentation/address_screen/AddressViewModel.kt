package com.hasanalic.ecommerce.feature_checkout.presentation.address_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressUseCases: AddressUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
): ViewModel() {

    private var _addressState = MutableLiveData(AddressState())
    val addressState: LiveData<AddressState> = _addressState

    fun getAddressList() {
        _addressState.value = AddressState(isLoading = true)

        viewModelScope.launch {
            val userId = sharedPreferencesUseCases.getUserIdUseCase()!!
            when(val result = addressUseCases.getAddressListByUserIdUseCase(userId)) {
                is Result.Error -> handleGetAddressListError(result.error)
                is Result.Success -> _addressState.value = AddressState(addressList = result.data)
            }
        }
    }

    private fun handleGetAddressListError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Could not get address list."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _addressState.value = AddressState(dataError = message)
    }

    fun selectAddress(itemIndex: Int) {
        val currentAddressList = _addressState.value!!.addressList

        currentAddressList.forEachIndexed { i, address ->
            if (i == itemIndex) {
                address.isSelected = true
            } else {
                address.isSelected = false
            }
        }

        _addressState.value = _addressState.value!!.copy(
            addressList = currentAddressList
        )
    }
}