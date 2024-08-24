package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_checkout.data.repository.FakeCardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardValidatorUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardByUserIdAndCardIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardsByUserIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertCardEntityUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CardsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var cardRepository: CardRepository
    private lateinit var cardUseCases: CardUseCases
    private lateinit var cardsViewModel: CardsViewModel

    @Before
    fun setup() {
        cardRepository = FakeCardRepository()
        cardUseCases = CardUseCases(
            getCardByUserIdAndCardIdUseCase = GetCardByUserIdAndCardIdUseCase(cardRepository),
            getCardsByUserIdUseCase = GetCardsByUserIdUseCase(cardRepository),
            insertCardEntityUseCase = InsertCardEntityUseCase(cardRepository),
            cardValidatorUseCase = CardValidatorUseCase()
        )
        cardsViewModel = CardsViewModel(cardUseCases)
    }

    @Test
    fun `getUserCards should returns card list when found`() {
        cardsViewModel.getUserCards("1")
        val state = cardsViewModel.cardsState.getOrAwaitValue()

        assertThat(state.cardList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `getUserCards should triggers data error when not found`() {
        cardsViewModel.getUserCards("2")
        val state = cardsViewModel.cardsState.getOrAwaitValue()

        assertThat(state.cardList).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNotEmpty()
    }
}