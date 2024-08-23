package com.hasanalic.ecommerce.feature_checkout.presentation.address_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_checkout.domain.model.Address
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
class AddressViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var addressRepository: AddressRepository
    private lateinit var addressUseCases: AddressUseCases
    private lateinit var addressViewModel: AddressViewModel

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
        addressViewModel = AddressViewModel(addressUseCases)
    }

    @Test
    fun `getAddressList should returns address list successfuly`() {
        addressViewModel.getAddressList("1")
        val state = addressViewModel.addressState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.addressList).isNotEmpty()
        assertThat(state.addressList.first()).isInstanceOf(Address::class.java)
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `selectAddress should set isSelected value of the clicked address to true`() {
        addressViewModel.getAddressList("1")
        addressViewModel.selectAddress(0)
        val state = addressViewModel.addressState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.addressList).isNotEmpty()
        assertThat(state.addressList.first().isSelected).isTrue()
        assertThat(state.dataError).isNull()
    }
}