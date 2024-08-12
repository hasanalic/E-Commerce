package com.hasanalic.ecommerce.feature_location.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.domain.model.AddressValidationError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddressValidatorUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addressValidatorUseCase: AddressValidatorUseCase

    @Before
    fun setup() {
        addressValidatorUseCase = AddressValidatorUseCase()
    }

    @Test
    fun `empty address title returns EMPTY_ADDRESS_TITLE`() {
        val result = addressValidatorUseCase("","detail")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(AddressValidationError.EMPTY_ADDRESS_TITLE)
    }

    @Test
    fun `empty address detail returns EMPTY_ADDRESS_DETAIL`() {
        val result = addressValidatorUseCase("title","")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(AddressValidationError.EMPTY_ADDRESS_DETAIL)
    }
}