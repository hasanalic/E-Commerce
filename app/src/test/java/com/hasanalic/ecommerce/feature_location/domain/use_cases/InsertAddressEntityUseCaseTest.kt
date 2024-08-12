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
class InsertAddressEntityUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var addressRepository: AddressRepository
    private lateinit var insertAddressEntityUseCase: InsertAddressEntityUseCase

    @Before
    fun setup() {
        addressRepository = FakeAddressRepository()
        insertAddressEntityUseCase = InsertAddressEntityUseCase(addressRepository)
    }
    
    @Test
    fun `Insert Address Entity should return success`() = runBlocking {
        val addressEntity = AddressEntity("1","title","detail")
        val result = insertAddressEntityUseCase(addressEntity)
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(Unit)
    }
}