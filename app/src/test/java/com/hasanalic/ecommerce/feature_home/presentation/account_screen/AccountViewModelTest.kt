package com.hasanalic.ecommerce.feature_home.presentation.account_screen

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
import com.hasanalic.ecommerce.feature_home.data.repository.FakeUserRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.UserRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.user_use_cases.GetUserUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.user_use_cases.UserUseCases
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var userRepository: UserRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var userUseCases: UserUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var accountViewModel: AccountViewModel

    @Before
    fun setup() {
        userRepository = FakeUserRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        userUseCases = UserUseCases(getUserUseCase = GetUserUseCase(userRepository))
        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        accountViewModel = AccountViewModel(sharedPreferencesUseCases, userUseCases)
    }

    @Test
    fun `getUser should fetches user and update the account state when user found at shared preferences`() {
        sharedPreferencesUseCases.saveUserIdUseCase("1")
        accountViewModel.getUser()
        val state = accountViewModel.accountState.getOrAwaitValue()

        assertThat(state.user).isNotNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
        assertThat(state.isUserLoggedOut).isFalse()
    }

    @Test
    fun `logOutUser successfuly log out user and update the account state`() {
        sharedPreferencesUseCases.saveUserIdUseCase("1")
        accountViewModel.logOutUser()

        val state = accountViewModel.accountState.getOrAwaitValue()
        val updatedUserId = sharedPreferencesUseCases.getUserIdUseCase()

        assertThat(updatedUserId).isNull()
        assertThat(state.isUserLoggedOut).isTrue()
        assertThat(state.isLoading).isFalse()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `logOutUser triggers action error when user id not found`() {
        accountViewModel.logOutUser()

        val state = accountViewModel.accountState.getOrAwaitValue()

        assertThat(state.isUserLoggedOut).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.actionError).isNotEmpty()
        assertThat(state.dataError).isNull()
    }
}