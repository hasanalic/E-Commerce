package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

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
            insertCardEntityUseCase = InsertCardEntityUseCase(cardRepository),
            cardValidatorUseCase = CardValidatorUseCase()
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

    @Test
    fun `onClickConfirm sets canUserContinueToNextStep to true when validation successful`() {
        val cardName = "card name"
        val cardNumber = "1234123412341234"
        val month = "12"
        val year = "24"
        val cvv = "123"

        paymentCardViewModel.onClickConfirm(cardName, cardNumber, month, year, cvv)
        val state = paymentCardViewModel.paymentCardState.getOrAwaitValue()

        assertThat(state.canUserContinueToNextStep).isTrue()
        assertThat(state.isLoading).isFalse()
        assertThat(state.doesUserHaveCards).isFalse()
        assertThat(state.cardId).isNull()
        assertThat(state.validationError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `onClickConfirm sets canUserContinueToNextStep to false when validation error`() {
        val cardName = ""
        val cardNumber = "1234123412341234"
        val month = "12"
        val year = "24"
        val cvv = "123"

        paymentCardViewModel.onClickConfirm(cardName, cardNumber, month, year, cvv)
        val state = paymentCardViewModel.paymentCardState.getOrAwaitValue()

        assertThat(state.canUserContinueToNextStep).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.doesUserHaveCards).isFalse()
        assertThat(state.cardId).isNull()
        assertThat(state.validationError).isNotEmpty()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `onClickConfirmWithSaveCard inserts card and sets canUserContinueToNextStep to true and cardId to a valid id`() {
        val cardName = "card name"
        val cardNumber = "1234123412341234"
        val month = "12"
        val year = "24"
        val cvv = "123"
        val userId = "1"

        paymentCardViewModel.onClickConfirmWithSaveCard(cardName, cardNumber, month, year, cvv, userId)
        val state = paymentCardViewModel.paymentCardState.getOrAwaitValue()

        assertThat(state.canUserContinueToNextStep).isTrue()
        assertThat(state.isLoading).isFalse()
        assertThat(state.doesUserHaveCards).isFalse()
        assertThat(state.cardId).isNotEmpty()
        assertThat(state.validationError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `onClickConfirmWithSaveCard triggers validationError when validation error`() {
        val cardName = ""
        val cardNumber = "1234123412341234"
        val month = "12"
        val year = "24"
        val cvv = "123"
        val userId = "1"

        paymentCardViewModel.onClickConfirmWithSaveCard(cardName, cardNumber, month, year, cvv, userId)
        val state = paymentCardViewModel.paymentCardState.getOrAwaitValue()

        assertThat(state.canUserContinueToNextStep).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.doesUserHaveCards).isFalse()
        assertThat(state.cardId).isNull()
        assertThat(state.validationError).isNotEmpty()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
    }
}