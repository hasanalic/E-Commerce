package com.hasanalic.ecommerce.feature_location.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.GetUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.IsDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.LogOutUserUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SaveUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SetDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_location.data.repository.FakeAddressRepository
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressUseCases
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressValidatorUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.DeleteUserAddressUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetAddressEntityByUserIdAndAddressIdUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetLocationListByUserIdUseCase
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
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var addressUseCases: AddressUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var locationViewModel: LocationViewModel

    @Before
    fun setup() {
        addressRepository = FakeAddressRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        addressUseCases = AddressUseCases(
            deleteUserAddressUseCase = DeleteUserAddressUseCase(addressRepository),
            getAddressEntityByUserIdAndAddressIdUseCase = GetAddressEntityByUserIdAndAddressIdUseCase(addressRepository),
            getLocationListByUserIdUseCase = GetLocationListByUserIdUseCase(addressRepository),
            getAddressListByUserIdUseCase = GetAddressListByUserIdUseCase(addressRepository),
            insertAddressEntityUseCase = InsertAddressEntityUseCase(addressRepository),
            addressValidatorUseCase = AddressValidatorUseCase()
        )
        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        sharedPreferencesDataSource.saveUserId("1")
        locationViewModel = LocationViewModel(addressUseCases, sharedPreferencesUseCases)
    }

    @Test
    fun `getAddressEntityList successfuly returns address entity list`() {
        locationViewModel.getUserAddressEntityListIfUserLoggedIn()
        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.locationList).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNull()
    }

    @Test
    fun `deleteAddressEntity successfuly deletes address entity and update the address list`() {
        locationViewModel.getUserAddressEntityListIfUserLoggedIn()
        locationViewModel.deleteLocation(0)

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.locationList).isEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isTrue()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNull()
    }

    @Test
    fun `deleteAddressEntity triggers action error when deletion fails`() {
        locationViewModel.getUserAddressEntityListIfUserLoggedIn()
        locationViewModel.deleteLocation(10)

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.locationList).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNotEmpty()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNull()
    }

    @Test
    fun `insertAddressEntity triggers validation error when title is empty`() {
        locationViewModel.getUserAddressEntityListIfUserLoggedIn()
        locationViewModel.title.value = ""
        locationViewModel.detail.value = "detail"
        locationViewModel.insertAddressEntity()

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNotEmpty()
    }

    @Test
    fun `insertAddressEntity triggers validation error when detail is empty`() {
        locationViewModel.getUserAddressEntityListIfUserLoggedIn()
        locationViewModel.title.value = "title"
        locationViewModel.detail.value = ""
        locationViewModel.insertAddressEntity()

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isFalse()
        assertThat(state.validationError).isNotEmpty()
    }

    @Test
    fun `insertAddressEntity successfuly when credentials are valid`() {
        locationViewModel.getUserAddressEntityListIfUserLoggedIn()
        locationViewModel.title.value = "title"
        locationViewModel.detail.value = "detail"
        locationViewModel.insertAddressEntity()

        val state = locationViewModel.locationState.getOrAwaitValue()

        assertThat(state.locationList).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.isAddressDeletionSuccessful).isFalse()
        assertThat(state.isAddressInsertionSuccessful).isTrue()
        assertThat(state.validationError).isNull()
    }

}