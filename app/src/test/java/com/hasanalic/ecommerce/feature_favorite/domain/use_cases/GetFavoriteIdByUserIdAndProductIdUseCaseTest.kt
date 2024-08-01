package com.hasanalic.ecommerce.feature_favorite.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_favorite.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_favorite.domain.repository.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoriteIdByUserIdAndProductIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var getFavoriteIdByUserIdAndProductIdUseCase: GetFavoriteIdByUserIdAndProductIdUseCase

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        getFavoriteIdByUserIdAndProductIdUseCase = GetFavoriteIdByUserIdAndProductIdUseCase(favoriteRepository)
    }

    @Test
    fun `Get FavoriteId should return success with id when parameters are in the db`() = runBlocking {
        val result = getFavoriteIdByUserIdAndProductIdUseCase("1","2")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(1)
    }

    @Test
    fun `Get FavoriteId should return data error NOT_FOUND when parameters are not in the db`() = runBlocking {
        val result = getFavoriteIdByUserIdAndProductIdUseCase("2","2")

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(DataError.Local.NOT_FOUND)
    }

}