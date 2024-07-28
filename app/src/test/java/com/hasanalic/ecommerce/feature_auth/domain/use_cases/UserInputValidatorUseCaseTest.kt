package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.InputValidationError
import org.junit.Before
import org.junit.Test

class UserInputValidatorUseCaseTest {

    private lateinit var userInputValidatorUseCase: UserInputValidatorUseCase

    @Before
    fun setup() {
        userInputValidatorUseCase = UserInputValidatorUseCase()
    }

    @Test
    fun `test empty name field returns InputValidationError EMPTY_NAME`() {
        val result = userInputValidatorUseCase("", "email", "pass")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(InputValidationError.EMPTY_NAME)

        //assertTrue(result is Result.Error && (result as Result.Error).error == InputValidationError.EMPTY_NAME)
    }

    @Test
    fun `test empty email field returns InputValidationError EMPTY_EMAIL`() {
        val result = userInputValidatorUseCase("name", "", "pass")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(InputValidationError.EMPTY_EMAIL)
    }

    @Test
    fun `test empty password field returns InputValidationError EMPTY_PASSWORD`() {
        val result = userInputValidatorUseCase("name", "email", "")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(InputValidationError.EMPTY_PASSWORD)
    }

    @Test
    fun `test valid fields return Result Success`() {
        val result = userInputValidatorUseCase("name", "email", "password")
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}