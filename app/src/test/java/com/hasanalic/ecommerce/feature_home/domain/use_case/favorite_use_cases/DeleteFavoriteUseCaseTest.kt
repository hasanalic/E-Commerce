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
class DeleteFavoriteUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        deleteFavoriteUseCase = DeleteFavoriteUseCase(favoriteRepository)
    }

    @Test
    fun `Delete Favorite should return success when favorite are deleted`() = runBlocking {
        val result = deleteFavoriteUseCase("1","1")
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}