package com.hasanalic.ecommerce.feature_auth.presentation.register

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
class RegisterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeAuthenticationRepository: AuthenticationRepository
    private lateinit var authUseCases: AuthUseCases
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setup() {
        fakeAuthenticationRepository = FakeAuthenticationRepository()
        authUseCases = AuthUseCases(
            insertUserUseCase = InsertUserUseCase(fakeAuthenticationRepository),
            getUserByEmailAndPassUseCase = GetUserByEmailAndPassUseCase(fakeAuthenticationRepository),
            userEmailValidatorUseCase = UserEmailValidatorUseCase(),
            userInputValidatorUseCase = UserInputValidatorUseCase(),
            userPasswordValidatorUseCase = UserPasswordValidatorUseCase()
        )
        registerViewModel = RegisterViewModel(authUseCases)
    }

    @Test
    fun `onRegisterClick with valid credentials triggers register state success`() {
        val name = "username"
        val email = "name@example.com"
        val password = "Password123"

        registerViewModel.onRegisterClick(name, email, password)

        val registerStateValue = registerViewModel.registerState.getOrAwaitValue()

        assertThat(registerStateValue.isRegistrationSuccessful).isTrue()
        assertThat(registerStateValue.isLoading).isFalse()
        assertThat(registerStateValue.dataError).isNull()
        assertThat(registerStateValue.validationError).isNull()
    }

    @Test
    fun `onRegisterClick with empty credentials triggers register state validation error`() {
        val emptyName = ""
        val emptyEmail = ""
        val emptyPassword = ""

        registerViewModel.onRegisterClick(emptyName, emptyEmail, emptyPassword)

        val registerStateValue = registerViewModel.registerState.getOrAwaitValue()

        assertThat(registerStateValue.validationError).isNotEmpty()
        assertThat(registerStateValue.isRegistrationSuccessful).isFalse()
        assertThat(registerStateValue.isLoading).isFalse()
        assertThat(registerStateValue.dataError).isNull()
    }

    @Test
    fun `onRegisterClick with empty name triggers register state validation error`() {
        val emptyName = ""
        val email = "name@example.com"
        val password = "Password123"

        registerViewModel.onRegisterClick(emptyName, email, password)

        val registerStateValue = registerViewModel.registerState.getOrAwaitValue()

        assertThat(registerStateValue.validationError).isNotEmpty()
        assertThat(registerStateValue.isRegistrationSuccessful).isFalse()
        assertThat(registerStateValue.isLoading).isFalse()
        assertThat(registerStateValue.dataError).isNull()
    }

    @Test
    fun `onRegisterClick with empty email triggers register state validation error`() {
        val name = ""
        val emptyEmail = "name@example.com"
        val password = "Password123"

        registerViewModel.onRegisterClick(name, emptyEmail, password)

        val registerStateValue = registerViewModel.registerState.getOrAwaitValue()

        assertThat(registerStateValue.validationError).isNotEmpty()
        assertThat(registerStateValue.isRegistrationSuccessful).isFalse()
        assertThat(registerStateValue.isLoading).isFalse()
        assertThat(registerStateValue.dataError).isNull()
    }

    @Test
    fun `onRegisterClick with empty password triggers register state validation error`() {
        val name = "name"
        val email = "name@example.com"
        val emptyPassword = ""

        registerViewModel.onRegisterClick(name, email, emptyPassword)

        val registerStateValue = registerViewModel.registerState.getOrAwaitValue()

        assertThat(registerStateValue.validationError).isNotEmpty()
        assertThat(registerStateValue.isRegistrationSuccessful).isFalse()
        assertThat(registerStateValue.isLoading).isFalse()
        assertThat(registerStateValue.dataError).isNull()
    }

}