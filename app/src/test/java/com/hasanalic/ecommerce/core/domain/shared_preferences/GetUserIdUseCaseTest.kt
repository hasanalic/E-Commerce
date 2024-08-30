package com.hasanalic.ecommerce.core.domain.shared_preferences

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.GetUserIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource
    private lateinit var getUserIdUseCase: GetUserIdUseCase

    @Before
    fun setup() {
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()
        getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource)
    }

    @Test
    fun `Get UserId returns null when userId not found`() {
        val userId = getUserIdUseCase()
        assertThat(userId).isNull()
    }

    @Test
    fun `Get UserId returns saved userId when found`() {
        val userId = "1"
        (sharedPreferencesDataSource as FakeSharedPreferencesDataSourceImp).fakeUserId = userId

        val fetchedUserId = getUserIdUseCase()
        assertThat(fetchedUserId).isEqualTo(userId)
    }
}