package com.hasanalic.ecommerce.core.domain.database_initialization

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeDatabaseInitializer
import com.hasanalic.ecommerce.core.domain.repository.DatabaseInitializer
import com.hasanalic.ecommerce.core.domain.use_cases.database_initialization.InsertDefaultProductsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertDefaultProductsUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var databaseInitializer: DatabaseInitializer
    private lateinit var insertDefaultProductsUseCase: InsertDefaultProductsUseCase

    @Before
    fun setup() {
        databaseInitializer = FakeDatabaseInitializer()
        insertDefaultProductsUseCase = InsertDefaultProductsUseCase(databaseInitializer)
    }

    @Test
    fun `Insert Default Products successfult inserts default products`() = runBlocking {
        insertDefaultProductsUseCase()
        val insertedProducts = (databaseInitializer as FakeDatabaseInitializer).insertedProducts

        assertThat(insertedProducts).isNotEmpty()
    }
}