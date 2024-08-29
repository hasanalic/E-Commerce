package com.hasanalic.ecommerce.core.domain.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeDatabaseInitializer
import com.hasanalic.ecommerce.core.domain.repository.DatabaseInitializer
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DatabaseInitializerUtilTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var databaseInitializer: DatabaseInitializer
    private lateinit var databaseInitializerUtil: DatabaseInitializerUtil

    @Before
    fun setup() {
        databaseInitializer = FakeDatabaseInitializer()
        databaseInitializerUtil = DatabaseInitializerUtil(databaseInitializer)
    }

    @Test
    fun `insertProductEntities should successfuly insert product entities`() = runBlocking {
        databaseInitializerUtil.insertProductEntities()
        val insertedProducts = (databaseInitializer as FakeDatabaseInitializer).insertedProducts

        assertThat(insertedProducts).isNotEmpty()
        assertThat(insertedProducts.first()).isInstanceOf(ProductEntity::class.java)
    }

    @Test
    fun `insertReviews should successfuly insert review entities`() = runBlocking {
        databaseInitializerUtil.insertReviews()
        val insertedReviews = (databaseInitializer as FakeDatabaseInitializer).insertedReviews

        assertThat(insertedReviews).isNotEmpty()
        assertThat(insertedReviews.first()).isInstanceOf(ReviewEntity::class.java)
    }
}