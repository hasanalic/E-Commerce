package com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoriteListByUserIdUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var getFavoriteListByUserIdUseCase: GetFavoriteListByUserIdUseCase

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        getFavoriteListByUserIdUseCase = GetFavoriteListByUserIdUseCase(favoriteRepository)
    }

    @Test
    fun `Get Favorite List should return success with favorite list when userId are in db`() = runBlocking {
        val result = getFavoriteListByUserIdUseCase("1")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.first()).isNotNull()
        assertThat((result as Result.Success).data.first().userId).isEqualTo("1")
    }

    @Test
    fun `Get Favorite List should return data error NOT_FOUND when userId are not in db`() = runBlocking {
        val result = getFavoriteListByUserIdUseCase("2")

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(DataError.Local.NOT_FOUND)
    }

}