package com.hasanalic.ecommerce.feature_location.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.domain.model.AddressValidationError
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val addressUseCases: AddressUseCases
): ViewModel() {

    private var _locationState = MutableLiveData(LocationState())
    val locationState: LiveData<LocationState> = _locationState

    fun getAddressEntityList(userId: String) {
        _locationState.value = LocationState(isLoading = true)
        viewModelScope.launch {
            when(val result = addressUseCases.getAddressEntityListByUserIdUseCase(userId)) {
                is Result.Error -> handleGetAddressEntityListError(result.error)
                is Result.Success -> {
                    _locationState.value = LocationState(
                        addressEntityList = result.data.toMutableList()
                    )
                }
            }
        }
    }

    private fun handleGetAddressEntityListError(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _locationState.value = LocationState(
                    dataError = "Adresler getirilemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _locationState.value = LocationState(
                    dataError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }
    }

    fun deleteAddressEntity(userId: String, addressId: String, itemIndex: Int) {
        viewModelScope.launch {
            when(val result = addressUseCases.deleteUserAddressUseCase(userId, addressId)) {
                is Result.Error -> handleDeleteAddressEntityError(result.error)
                is Result.Success -> removeAddressFromList(itemIndex)
            }
        }
    }

    private fun handleDeleteAddressEntityError(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _locationState.value = _locationState.value!!.copy(
                    actionError = "Adres silinemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _locationState.value = _locationState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }
    }

    private fun removeAddressFromList(itemIndex: Int) {
        val currentAddressList = _locationState.value!!.addressEntityList

        currentAddressList.removeAt(itemIndex)

        _locationState.value = _locationState.value!!.copy(
            addressEntityList = currentAddressList,
            isAddressDeletionSuccessful = true
        )
    }

    fun insertAddressEntity(userId: String, title: String, detail: String) {
        val addressValidationResult = addressUseCases.addressValidatorUseCase(title, detail)
        if (addressValidationResult is Result.Error) {
            handleAddressValidationError(addressValidationResult.error)
            return
        }

        viewModelScope.launch {
            val addressEntity = AddressEntity(userId, title, detail)
            when(val result = addressUseCases.insertAddressEntityUseCase(addressEntity)) {
                is Result.Error -> handleInsertAddressEntityError(result.error)
                is Result.Success -> {
                    _locationState.value = _locationState.value!!.copy(
                        isAddressInsertionSuccessful = true
                    )
                    getAddressEntityList(userId)
                }
            }
        }
    }

    private fun handleAddressValidationError(error: AddressValidationError) {
        val errorMessage = when(error) {
            AddressValidationError.EMPTY_ADDRESS_TITLE -> { "Addres başlığı boş olamaz." }
            AddressValidationError.EMPTY_ADDRESS_DETAIL -> { "Addres detayı boş olamaz." }
        }

        _locationState.value = _locationState.value!!.copy(
            validationError = errorMessage
        )
    }

    private fun handleInsertAddressEntityError(error: DataError.Local) {
        when(error) {
            DataError.Local.INSERTION_FAILED -> {
                _locationState.value = LocationState(
                    dataError = "Adres kaydedilemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _locationState.value = LocationState(
                    dataError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }
    }
}