package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailValidationError
import org.junit.Before
import org.junit.Test

class UserEmailValidatorUseCaseTest {

    private lateinit var userEmailValidatorUseCase: UserEmailValidatorUseCase

    @Before
    fun setup() {
        userEmailValidatorUseCase = UserEmailValidatorUseCase()
    }

    @Test
    fun `test invalid email field returns EmailValidationError INVALID_FORMAT`() {
        val result = userEmailValidatorUseCase("email")
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(EmailValidationError.INVALID_FORMAT)
    }

    @Test
    fun `test valid email field returns Result Success`() {
        val result = userEmailValidatorUseCase("email@domain.com")
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}