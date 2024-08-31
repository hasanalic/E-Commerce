package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.data.repository.FakeAuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginUserWithEmailAndPasswordUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var loginUserWithEmailAndPasswordUseCase: LoginUserWithEmailAndPasswordUseCase

    @Before
    fun setup() {
        authenticationRepository = FakeAuthenticationRepository()
        loginUserWithEmailAndPasswordUseCase = LoginUserWithEmailAndPasswordUseCase(authenticationRepository)
    }

    @Test
    fun `Login User With Email And Password should return success with user id when credentials are correct`() = runBlocking {
        val userId = 1
        val email = "john.doe@example.com"
        val password = "Password123"

        val result = loginUserWithEmailAndPasswordUseCase(email, password)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(userId)
    }

    @Test
    fun `Login User With Email And Password should return error when User not found`() = runBlocking {
        val nonExistentEmail = "nonexistent@example.com"
        val nonExistentPassword = "nonExistentPassword1"

        val result = loginUserWithEmailAndPasswordUseCase(nonExistentEmail, nonExistentPassword)

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }
}