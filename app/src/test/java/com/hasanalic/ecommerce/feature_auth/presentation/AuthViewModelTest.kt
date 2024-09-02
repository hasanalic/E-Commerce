package com.hasanalic.ecommerce.feature_auth.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeDatabaseInitializer
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.DatabaseInitializer
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.database_initialization.DatabaseInitializerUseCases
import com.hasanalic.ecommerce.core.domain.use_cases.database_initialization.InsertDefaultProductsUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.database_initialization.InsertDefaultReviewsUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.GetUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.IsDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.LogOutUserUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SaveUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SetDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource
    private lateinit var databaseInitializer: DatabaseInitializer

    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases
    private lateinit var databaseInitializerUseCases: DatabaseInitializerUseCases

    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() {
        databaseInitializer = FakeDatabaseInitializer()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        databaseInitializerUseCases = DatabaseInitializerUseCases(
            insertDefaultProductsUseCase = InsertDefaultProductsUseCase(databaseInitializer),
            insertDefaultReviewsUseCase = InsertDefaultReviewsUseCase(databaseInitializer)
        )
    }

    @Test
    fun `checkIfUserAlreadyLoggedIn should sets isUserAlreadyLoggedIn to true when userId found`() {
        sharedPreferencesUseCases.saveUserIdUseCase("1")
        authViewModel = AuthViewModel(sharedPreferencesUseCases, databaseInitializerUseCases)

        val state = authViewModel.authState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.isDatabaseInitialized).isTrue()
        assertThat(state.isUserAlreadyLoggedIn).isTrue()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `checkIfUserAlreadyLoggedIn should sets isUserAlreadyLoggedIn to false when userId is null`() {
        authViewModel = AuthViewModel(sharedPreferencesUseCases, databaseInitializerUseCases)

        val state = authViewModel.authState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.isDatabaseInitialized).isTrue()
        assertThat(state.isUserAlreadyLoggedIn).isFalse()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `checkIsDatabaseInitialized should sets isDatabaseInitialized to true when insertion done`() {
        authViewModel = AuthViewModel(sharedPreferencesUseCases, databaseInitializerUseCases)

        val state = authViewModel.authState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.isDatabaseInitialized).isTrue()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `checkIsDatabaseInitialized should sets isDatabaseInitialized to true when insertion already done`() {
        (sharedPreferencesDataSource as FakeSharedPreferencesDataSourceImp).isInitialized = true
        authViewModel = AuthViewModel(sharedPreferencesUseCases, databaseInitializerUseCases)

        val state = authViewModel.authState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.isDatabaseInitialized).isTrue()
        assertThat(state.dataError).isNull()
    }
}