package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.data.repository.FakeAuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertUserUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var insertUserUseCase: InsertUserUseCase

    @Before
    fun setup() {
        authenticationRepository = FakeAuthenticationRepository()
        insertUserUseCase = InsertUserUseCase(authenticationRepository)
    }

    @Test
    fun `Insert User should return success with id when user is registered successfuly`() = runBlocking {
        val expectedUserId = 1L
        val name = "John Doe"
        val email = "john.doe@example.com"
        val password = "Password123"

        val result = insertUserUseCase(name, email, password)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedUserId)
    }
}