package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_checkout.data.repository.FakeCardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardByUserIdAndCardIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardsByUserIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertCardEntityUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PaymentCardViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var cardRepository: CardRepository
    private lateinit var cardUseCases: CardUseCases
    private lateinit var paymentCardViewModel: PaymentCardViewModel

    @Before
    fun setup() {
        cardRepository = FakeCardRepository()
        cardUseCases = CardUseCases(
            getCardByUserIdAndCardIdUseCase = GetCardByUserIdAndCardIdUseCase(cardRepository),
            getCardsByUserIdUseCase = GetCardsByUserIdUseCase(cardRepository),
            insertCardEntityUseCase = InsertCardEntityUseCase(cardRepository)
        )
        paymentCardViewModel = PaymentCardViewModel(cardUseCases)
    }

    @Test
    fun `checkIfUserHaveAnyCard sets doesUserHaveCards to true when user have cards`() {
        paymentCardViewModel.checkIfUserHaveAnyCard("1")
        val state = paymentCardViewModel.paymentCardState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.doesUserHaveCards).isTrue()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `checkIfUserHaveAnyCard sets doesUserHaveCards to false when user dont have any cards`() {
        val state = paymentCardViewModel.paymentCardState.getOrAwaitValue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.doesUserHaveCards).isFalse()
        assertThat(state.actionError).isNull()
    }
}