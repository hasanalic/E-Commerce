package com.hasanalic.ecommerce.feature_auth.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_auth.data.repository.FakeAuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.GetUserByEmailAndPassUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.InsertUserUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserEmailValidatorUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserInputValidatorUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserPasswordValidatorUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var authUseCases: AuthUseCases
    private lateinit var authenticationRepository: AuthenticationRepository

    @Before
    fun setup() {
        authenticationRepository = FakeAuthenticationRepository()
        authUseCases = AuthUseCases(
            insertUserUseCase = InsertUserUseCase(authenticationRepository),
            getUserByEmailAndPassUseCase = GetUserByEmailAndPassUseCase(authenticationRepository),
            userInputValidatorUseCase = UserInputValidatorUseCase(),
            userEmailValidatorUseCase = UserEmailValidatorUseCase(),
            userPasswordValidatorUseCase = UserPasswordValidatorUseCase()
        )
        loginViewModel = LoginViewModel(authUseCases)
    }

    @Test
    fun `onLoginClick with valid credentials triggers login success`()  {
        val email = "name@example.com"
        val password = "Password123"

        loginViewModel.onLoginClick(email, password)

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.isLoginSuccessful).isTrue()
        assertThat(loginStateValue.isLoading).isFalse()
        assertThat(loginStateValue.dataError).isNull()
        assertThat(loginStateValue.validationError).isNull()
    }

    @Test
    fun `onLoginClick with empty credentials triggers login validation error`()  {
        val email = ""
        val password = ""

        loginViewModel.onLoginClick(email, password)

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.validationError).isNotEmpty()
        assertThat(loginStateValue.isLoginSuccessful).isFalse()
        assertThat(loginStateValue.isLoading).isFalse()
        assertThat(loginStateValue.dataError).isNull()
    }

    @Test
    fun `onLoginClick with invalid email format triggers login validation error`() {
        val invalidEmail = "email"
        val password = "Password123"

        loginViewModel.onLoginClick(invalidEmail, password)

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.validationError).isNotEmpty()
        assertThat(loginStateValue.isLoginSuccessful).isFalse()
        assertThat(loginStateValue.isLoading).isFalse()
        assertThat(loginStateValue.dataError).isNull()
    }

    @Test
    fun `onLoginClick with empty email triggers login validation error`() {
        val emptyEmail = ""
        val password = "Password123"

        loginViewModel.onLoginClick(emptyEmail, password)

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.validationError).isNotEmpty()
        assertThat(loginStateValue.isLoginSuccessful).isFalse()
        assertThat(loginStateValue.isLoading).isFalse()
        assertThat(loginStateValue.dataError).isNull()
    }

    @Test
    fun `onLoginClick with invalid password format triggers login validation error`() {
        val email = "name@example.com"
        val invalidPassword = "pass"

        loginViewModel.onLoginClick(email, invalidPassword)

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.validationError).isNotEmpty()
        assertThat(loginStateValue.isLoginSuccessful).isFalse()
        assertThat(loginStateValue.isLoading).isFalse()
        assertThat(loginStateValue.dataError).isNull()
    }

    @Test
    fun `onLoginClick with empty password triggers login validation error`() {
        val email = "name@example.com"
        val emptyPassword = ""

        loginViewModel.onLoginClick(email, emptyPassword)

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.validationError).isNotEmpty()
        assertThat(loginStateValue.isLoginSuccessful).isFalse()
        assertThat(loginStateValue.isLoading).isFalse()
        assertThat(loginStateValue.dataError).isNull()
    }

    @Test
    fun `onLoginClick with non-existent user triggers login data error`() {
        val nonExistentEmail = "nonexistent@example.com"
        val nonExistentPassword = "nonExistentPassword1"

        loginViewModel.onLoginClick(nonExistentEmail, nonExistentPassword)

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.dataError).isNotEmpty()
        assertThat(loginStateValue.validationError).isNull()
        assertThat(loginStateValue.isLoginSuccessful).isFalse()
        assertThat(loginStateValue.isLoading).isFalse()
    }

}