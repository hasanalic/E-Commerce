package com.hasanalic.ecommerce.feature_checkout.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_checkout.data.repository.FakeCardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertCardEntityUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertCardEntityUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var cardRepository: CardRepository
    private lateinit var insertCardEntityUseCase: InsertCardEntityUseCase

    @Before
    fun setup() {
        cardRepository = FakeCardRepository()
        insertCardEntityUseCase = InsertCardEntityUseCase(cardRepository)
    }

    @Test
    fun `Insert Card Entity returns card id when insertion successful`() = runBlocking {
        val cardEntity = CardEntity("name","1234123412341234","1")
        val result = insertCardEntityUseCase(cardEntity)
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo("1")
    }
}