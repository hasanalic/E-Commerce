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
class GetFavoriteProductsUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var getFavoritesUseCase: GetFavoriteProductsUseCase

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        getFavoritesUseCase = GetFavoriteProductsUseCase(favoriteRepository)
    }

    @Test
    fun `Get Favorite Products should return success with favorite product list when userId are valid`() = runBlocking {
        val result = getFavoritesUseCase("1")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.first()).isNotNull()
        assertThat((result as Result.Success).data.first().productId).isEqualTo("1")
    }
}