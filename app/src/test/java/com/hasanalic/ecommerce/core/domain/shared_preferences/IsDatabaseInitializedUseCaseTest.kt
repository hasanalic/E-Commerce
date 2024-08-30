package com.hasanalic.ecommerce.core.domain.shared_preferences

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.IsDatabaseInitializedUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class IsDatabaseInitializedUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource
    private lateinit var isDatabaseInitializedUseCase: IsDatabaseInitializedUseCase

    @Before
    fun setup() {
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()
        isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource)
    }

    @Test
    fun `Is Database Initialized returns isInitialized value`() {
        (sharedPreferencesDataSource as FakeSharedPreferencesDataSourceImp).isInitialized = true
        val fetchedIsInitialized = isDatabaseInitializedUseCase()

        assertThat(fetchedIsInitialized).isTrue()
    }
}