package com.hasanalic.ecommerce.feature_location.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_location.data.repository.FakeAddressRepository
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressUseCases
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressValidatorUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.DeleteUserAddressUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetAddressEntityByUserIdAndAddressIdUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetAddressEntityListByUserIdUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetAddressListByUserIdUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.InsertAddressEntityUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LocationViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var addressRepository: AddressRepository
    private lateinit var addressUseCases: AddressUseCases
    private lateinit var locationViewModel: LocationViewModel

    @Before
    fun setup() {
        addressRepository = FakeAddressRepository()
        addressUseCases = AddressUseCases(
            deleteUserAddressUseCase = DeleteUserAddressUseCase(addressRepository),
            getAddressEntityByUserIdAndAddressIdUseCase = GetAddressEntityByUserIdAndAddressIdUseCase(addressRepository),
            getAddressEntityListByUserIdUseCase = GetAddressEntityListByUserIdUseCase(addressRepository),
            getAddressListByUserIdUseCase = GetAddressListByUserIdUseCase(addressRepository),
            insertAddressEntityUseCase = InsertAddressEntityUseCase(addressRepository),
            addressValidatorUseCase = AddressValidatorUseCase()
        )
        locationViewModel = LocationViewModel(addressUseCases)
    }

    @Test
    fun `getAddressEntityList successfuly returns address entity list`() {
        locationViewModel.getAddressEntityList("1")
        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.addressEntityList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNull()
    }

    @Test
    fun `deleteAddressEntity successfuly deletes address entity and update the address list`() {
        locationViewModel.getAddressEntityList("1")
        locationViewModel.deleteAddressEntity("1","1",0)

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.addressEntityList).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isTrue()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNull()
    }

    @Test
    fun `deleteAddressEntity triggers action error when deletion fails`() {
        locationViewModel.getAddressEntityList("1")
        locationViewModel.deleteAddressEntity("2","2",0)

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.addressEntityList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNotEmpty()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNull()
    }

    @Test
    fun `insertAddressEntity triggers validation error when title is empty`() {
        locationViewModel.insertAddressEntity("1","","detail")

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.addressEntityList).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNotEmpty()
    }

    @Test
    fun `insertAddressEntity triggers validation error when detail is empty`() {
        locationViewModel.insertAddressEntity("1","title","")

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.addressEntityList).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNotEmpty()
    }

    @Test
    fun `insertAddressEntity successfuly when credentials are valid`() {
        locationViewModel.insertAddressEntity("1","title","detail")

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.addressEntityList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNull()
    }

}