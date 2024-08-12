package com.hasanalic.ecommerce.feature_location.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.data.repository.FakeAddressRepository
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAddressListByUserIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var addressRepository: AddressRepository
    private lateinit var getAddressListByUserIdUseCase: GetAddressListByUserIdUseCase

    @Before
    fun setup() {
        addressRepository = FakeAddressRepository()
        getAddressListByUserIdUseCase = GetAddressListByUserIdUseCase(addressRepository)
    }
    
    @Test
    fun `Get Address List should return success with address list when it found`() = runBlocking {
        val result = getAddressListByUserIdUseCase("1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotEmpty()
    }
}