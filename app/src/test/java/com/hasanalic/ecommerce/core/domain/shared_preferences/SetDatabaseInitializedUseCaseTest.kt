package com.hasanalic.ecommerce.core.domain.shared_preferences

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SetDatabaseInitializedUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SetDatabaseInitializedUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource
    private lateinit var setDatabaseInitializedUseCase: SetDatabaseInitializedUseCase

    @Before
    fun setup() {
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()
        setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource)
    }

    @Test
    fun `Set Database Initialized sets isInitialized value`() {
        setDatabaseInitializedUseCase(true)
        val isInitialized = (sharedPreferencesDataSource as FakeSharedPreferencesDataSourceImp).isInitialized
        assertThat(isInitialized).isTrue()
    }
}