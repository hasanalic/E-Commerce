package com.hasanalic.ecommerce.feature_location.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.domain.model.AddressValidationError
import com.hasanalic.ecommerce.feature_location.domain.model.Location
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressUseCases
import com.hasanalic.ecommerce.feature_location.presentation.views.LocationListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val addressUseCases: AddressUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
): ViewModel(), LocationListener {

    var title = MutableLiveData("")
    var detail = MutableLiveData("")

    private var _locationState = MutableLiveData(LocationState())
    val locationState: LiveData<LocationState> = _locationState

    fun getUserAddressEntityListIfUserLoggedIn() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        if (userId == null) {
            _locationState.value = _locationState.value!!.copy(
                isLoading = false,
                isUserLoggedIn = false
            )
            return
        }

        getAddressEntityList(userId)
    }

    private fun getAddressEntityList(userId: String) {
        _locationState.value = _locationState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            when(val result = addressUseCases.getLocationListByUserIdUseCase(userId)) {
                is Result.Error -> handleGetAddressEntityListError(result.error)
                is Result.Success -> {
                    _locationState.value = _locationState.value!!.copy(
                        isLoading = false,
                        userId = userId,
                        locationList = result.data.toMutableList()
                    )
                }
            }
        }
    }

    private fun handleGetAddressEntityListError(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _locationState.value = LocationState(
                    dataError = "Addresses could not be retrieved."
                )
            }
            DataError.Local.UNKNOWN -> {
                _locationState.value = LocationState(
                    dataError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun deleteLocation(addressId: Int) {
        _locationState.value = _locationState.value!!.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val userId = _locationState.value!!.userId
            when(val result = addressUseCases.deleteUserAddressUseCase(userId, addressId.toString())) {
                is Result.Error -> handleDeleteAddressEntityError(result.error)
                is Result.Success -> removeLocationFromList(addressId)
            }
        }
    }

    private fun handleDeleteAddressEntityError(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _locationState.value = _locationState.value!!.copy(
                    isLoading = false,
                    actionError = "The address could not be deleted."
                )
            }
            DataError.Local.UNKNOWN -> {
                _locationState.value = _locationState.value!!.copy(
                    isLoading = false,
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    private fun removeLocationFromList(addressId: Int) {
        val currentAddressList = _locationState.value!!.locationList

        currentAddressList.removeAll { it.addressId == addressId }

        _locationState.value = _locationState.value!!.copy(
            isLoading = false,
            locationList = currentAddressList,
            isAddressDeletionSuccessful = true,

        )
    }

    fun insertAddressEntity() {
        val titleValue = title.value ?: ""
        val detailValue = detail.value ?: ""

        _locationState.value = _locationState.value!!.copy(
            isLoading = true,
            validationError = null,
            dataError = null,
            actionError = null
        )

        val addressValidationResult = addressUseCases.addressValidatorUseCase(titleValue, detailValue)
        if (addressValidationResult is Result.Error) {
            handleAddressValidationError(addressValidationResult.error)
            return
        }

        viewModelScope.launch {
            val userId = _locationState.value!!.userId
            val addressEntity = AddressEntity(userId, titleValue, detailValue)
            when(val result = addressUseCases.insertAddressEntityUseCase(addressEntity)) {
                is Result.Error -> handleInsertAddressEntityError(result.error)
                is Result.Success -> {
                    _locationState.value = _locationState.value!!.copy(
                        isAddressInsertionSuccessful = true,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleAddressValidationError(error: AddressValidationError) {
        val errorMessage = when(error) {
            AddressValidationError.EMPTY_ADDRESS_TITLE -> { "The address title cannot be empty." }
            AddressValidationError.EMPTY_ADDRESS_DETAIL -> { "The address detail cannot be empty." }
        }

        _locationState.value = _locationState.value!!.copy(
            validationError = errorMessage,
            isLoading = false
        )
    }

    private fun handleInsertAddressEntityError(error: DataError.Local) {
        val errorMessage = when(error) {
            DataError.Local.INSERTION_FAILED -> "Address could not be saved."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }

        _locationState.value = LocationState(
            dataError = errorMessage
        )
    }

    override fun onDeleteClicked(location: Location) {
        deleteLocation(location.addressId)
    }
}