package com.hasanalic.ecommerce.feature_favorite.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_favorite.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_favorite.domain.repository.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertFavoriteAndGetIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var insertFavoriteAndGetIdUseCase: InsertFavoriteAndGetIdUseCase

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        insertFavoriteAndGetIdUseCase = InsertFavoriteAndGetIdUseCase(favoriteRepository)
    }

    @Test
    fun `Insert Favorite should return success with id when inserting successful`() = runBlocking {
        val favoriteEntity = FavoritesEntity("1","1")
        val result = insertFavoriteAndGetIdUseCase(favoriteEntity)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(1L)
    }


}