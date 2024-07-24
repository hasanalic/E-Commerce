package com.hasanalic.ecommerce.feature_location.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.data.dto.AddressEntity
import com.hasanalic.ecommerce.domain.model.Address
import com.hasanalic.ecommerce.domain.repository.CheckoutRepository
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository
): ViewModel() {

    private var _statusSaveAddress = MutableLiveData<Resource<Boolean>>()
    val statusSaveAddress: LiveData<Resource<Boolean>>
        get() = _statusSaveAddress

    private var _statusAddressList = MutableLiveData<Resource<List<Address>>>()
    val statusAddressList: LiveData<Resource<List<Address>>>
        get() = _statusAddressList

    private var _statusDeleteAddress = MutableLiveData<Resource<Boolean>>()
    val statusDeleteAddress: LiveData<Resource<Boolean>>
        get() = _statusDeleteAddress

    fun saveAddress(userId: String, address: String, addressTitle: String) {
        _statusSaveAddress.value = Resource.Loading()
        viewModelScope.launch {
            val response = checkoutRepository.insertAddress(AddressEntity(
                userId, addressTitle, address
            ))
            _statusSaveAddress.value = response
        }
    }

    fun getAddressList(userId: String) {
        _statusAddressList.value = Resource.Loading()
        viewModelScope.launch {
            val response = checkoutRepository.getAddressesByUserId(userId)
            _statusAddressList.value = response
        }
    }

    fun deleteAddress(userId: String, addressId: String) {
        _statusDeleteAddress.value = Resource.Loading()
        viewModelScope.launch {
            val response = checkoutRepository.deleteAddress(userId, addressId)
            if (response is Resource.Success) {
                deleteAddressFromList(addressId)
            }
            _statusDeleteAddress.value = response
        }
    }

    private fun deleteAddressFromList(addressId: String) {
        var tempAddressList = _statusAddressList.value!!.data
        tempAddressList = tempAddressList?.filterIndexed { _, address ->
            address.addressId != addressId
        }
        _statusAddressList.value = Resource.Success(tempAddressList!!)
    }
}