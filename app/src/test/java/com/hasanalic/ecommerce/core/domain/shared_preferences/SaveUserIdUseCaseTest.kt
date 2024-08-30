package com.hasanalic.ecommerce.core.domain.shared_preferences

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SaveUserIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveUserIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource
    private lateinit var saveUserIdUseCase: SaveUserIdUseCase

    @Before
    fun setup() {
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()
        saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource)
    }

    @Test
    fun `Save UserId saves userId`() {
        val userId = "1"
        saveUserIdUseCase(userId)

        val fetchedUsId = (sharedPreferencesDataSource as FakeSharedPreferencesDataSourceImp).fakeUserId
        assertThat(fetchedUsId).isEqualTo(userId)
    }
}