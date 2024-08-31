package com.hasanalic.ecommerce.feature_home.domain.use_case.user_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.repository.FakeUserRepository
import com.hasanalic.ecommerce.feature_home.domain.model.User
import com.hasanalic.ecommerce.feature_home.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var userRepository: UserRepository
    private lateinit var getUserUseCase: GetUserUseCase

    @Before
    fun setup() {
        userRepository = FakeUserRepository()
        getUserUseCase = GetUserUseCase(userRepository)
    }

    @Test
    fun `Get User should returns user data class`() = runBlocking {
        val result = getUserUseCase("1")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isInstanceOf(User::class.java)
        assertThat((result as Result.Success).data).isNotNull()
    }
}