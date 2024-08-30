package com.hasanalic.ecommerce.core.domain.shared_preferences

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.LogOutUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LogOutUserUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource
    private lateinit var logOutUserUseCase: LogOutUserUseCase

    @Before
    fun setup() {
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()
        logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
    }

    @Test
    fun `LogOut User sets userId to null`() {
        logOutUserUseCase()
        val userId = (sharedPreferencesDataSource as FakeSharedPreferencesDataSourceImp).fakeUserId
        assertThat(userId).isNull()
    }
}