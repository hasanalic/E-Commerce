package com.hasanalic.ecommerce.feature_auth.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.GetUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.IsDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.LogOutUserUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SaveUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SetDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_auth.data.repository.FakeAuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.LoginUserWithEmailAndPasswordUseCase
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

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var authUseCases: AuthUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        authenticationRepository = FakeAuthenticationRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        authUseCases = AuthUseCases(
            insertUserUseCase = InsertUserUseCase(authenticationRepository),
            loginUserWithEmailAndPasswordUseCase = LoginUserWithEmailAndPasswordUseCase(authenticationRepository),
            userInputValidatorUseCase = UserInputValidatorUseCase(),
            userEmailValidatorUseCase = UserEmailValidatorUseCase(),
            userPasswordValidatorUseCase = UserPasswordValidatorUseCase()
        )

        loginViewModel = LoginViewModel(sharedPreferencesUseCases ,authUseCases)
    }

    @Test
    fun `onLoginClick with valid credentials triggers login success`()  {
        val email = "name@example.com"
        val password = "Password123"

        loginViewModel.email.value = email
        loginViewModel.password.value = password
        loginViewModel.onLoginClick()

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

        loginViewModel.email.value = email
        loginViewModel.password.value = password
        loginViewModel.onLoginClick()

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

        loginViewModel.email.value = invalidEmail
        loginViewModel.password.value = password
        loginViewModel.onLoginClick()

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

        loginViewModel.email.value = emptyEmail
        loginViewModel.password.value = password
        loginViewModel.onLoginClick()

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

        loginViewModel.email.value = email
        loginViewModel.password.value = invalidPassword
        loginViewModel.onLoginClick()

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

        loginViewModel.email.value = email
        loginViewModel.password.value = emptyPassword
        loginViewModel.onLoginClick()

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

        loginViewModel.email.value = nonExistentEmail
        loginViewModel.password.value = nonExistentPassword
        loginViewModel.onLoginClick()

        val loginStateValue = loginViewModel.loginState.getOrAwaitValue()

        assertThat(loginStateValue.dataError).isNotEmpty()
        assertThat(loginStateValue.validationError).isNull()
        assertThat(loginStateValue.isLoginSuccessful).isFalse()
        assertThat(loginStateValue.isLoading).isFalse()
    }

}