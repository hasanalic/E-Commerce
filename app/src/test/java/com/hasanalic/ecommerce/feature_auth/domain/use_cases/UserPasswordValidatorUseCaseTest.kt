package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.PasswordValidationError
import org.junit.Before
import org.junit.Test

class UserPasswordValidatorUseCaseTest {

    private lateinit var userPasswordValidatorUseCase: UserPasswordValidatorUseCase

    @Before
    fun setup() {
        userPasswordValidatorUseCase = UserPasswordValidatorUseCase()
    }

    @Test
    fun `test short password input returns PasswordValidationError TOO_SHORT`() {
        val result = userPasswordValidatorUseCase("123")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(PasswordValidationError.TOO_SHORT)
    }

    @Test
    fun `test lowercase password input returns PasswordValidationError NO_UPPERCASE`() {
        val result = userPasswordValidatorUseCase("password123")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(PasswordValidationError.NO_UPPERCASE)
    }

    @Test
    fun `test password without digit input returns PasswordValidationError NO_DIGIT`() {
        val result = userPasswordValidatorUseCase("Passwordddd")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(PasswordValidationError.NO_DIGIT)
    }

    @Test
    fun `test valid password input returns Result Success`() {
        val result = userPasswordValidatorUseCase("Password1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}