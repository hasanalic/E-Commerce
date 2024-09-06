package com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckIfProductInFavoritesUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var checkIfProductInFavoritesUseCase: CheckIfProductInFavoritesUseCase

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        checkIfProductInFavoritesUseCase = CheckIfProductInFavoritesUseCase(favoriteRepository)
    }

    @Test
    fun `Check If Product In Favorites should return success with true when favorite entity exists in db`() = runBlocking {
        val result = checkIfProductInFavoritesUseCase("1","1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(true)
    }

    @Test
    fun `Check If Product In Favorites should return success with false when favorite entity not found in db`() = runBlocking {
        val result = checkIfProductInFavoritesUseCase("1","2")
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(false)
    }
}