package com.hasanalic.ecommerce.feature_home.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.data.local.MyDatabase
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class FavoritesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: MyDatabase
    private lateinit var favoritesDao: FavoritesDao

    @Before
    fun setup() {
        hiltRule.inject()
        favoritesDao = database.favoritesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertFavoriteAndGetByUserId() = runBlocking {
        val userId = "1"
        val productId = "2"
        val favoriteEntity = FavoritesEntity(userId,productId)
        favoritesDao.insertFavorite(favoriteEntity)

        val favoritesList = favoritesDao.getFavorites(userId)

        assertThat(favoritesList).isNotNull()
        assertThat(favoritesList?.size).isEqualTo(1)
        assertThat(favoritesList?.get(0)?.userId).isEqualTo(userId)
    }

    @Test
    fun insertFavoriteAndGetByProductId() = runBlocking {
        val userId = "1"
        val productId = "2"
        val favoriteEntity = FavoritesEntity(userId,productId)
        favoritesDao.insertFavorite(favoriteEntity)

        val fetchedFavoriteEntityId = favoritesDao.getFavoriteByProductId(userId, productId)

        assertThat(fetchedFavoriteEntityId).isNotNull()
        assertThat(fetchedFavoriteEntityId).isEqualTo(1)
    }

    @Test
    fun deleteFavoriteAndVerify() = runBlocking {
        val userId = "1"
        val productId = "2"
        val favoriteEntity = FavoritesEntity(userId,productId)
        favoritesDao.insertFavorite(favoriteEntity)

        favoritesDao.deleteFavorite(userId, productId)

        val fetchedFavoritesList = favoritesDao.getFavorites(userId)
        println(fetchedFavoritesList)

        assertThat(fetchedFavoritesList.isNullOrEmpty()).isTrue()
    }
}