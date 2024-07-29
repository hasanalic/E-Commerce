package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.data.repository.FakeAuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.model.User
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserByEmailAndPassUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var getUserByEmailAndPassUseCase: GetUserByEmailAndPassUseCase

    @Before
    fun setup() {
        authenticationRepository = FakeAuthenticationRepository()
        getUserByEmailAndPassUseCase = GetUserByEmailAndPassUseCase(authenticationRepository)
    }

    @Test
    fun `Get User By Email and Password should return success with User when credentials are correct`() = runBlocking {
        val userId = 1
        val name = "John Doe"
        val email = "john.doe@example.com"
        val password = "Password123"

        val fakeUser = User(userId, name,  email)

        val result = getUserByEmailAndPassUseCase(email, password)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(fakeUser)
    }

}