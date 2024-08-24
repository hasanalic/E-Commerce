package com.hasanalic.ecommerce.feature_checkout.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.domain.model.CardValidationError
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardValidatorUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CardValidatorUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cardValidatorUseCase: CardValidatorUseCase

    @Before
    fun setup() {
        cardValidatorUseCase = CardValidatorUseCase()
    }

    @Test
    fun `valid inputs returns success`() {
        val cardName = "user name"
        val cardNumber = "1234123412341234"
        val month= "12"
        val year = "14"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `empty card name returns CardValidationError EMPTY_CARD_NAME`() {
        val cardName = ""
        val cardNumber = "1234123412341234"
        val month= "12"
        val year = "14"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.EMPTY_CARD_NAME)
    }

    @Test
    fun `short card name returns CardValidationError SHORT_CARD_NAME`() {
        val cardName = "na"
        val cardNumber = "1234123412341234"
        val month= "12"
        val year = "14"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.SHORT_CARD_NAME)
    }

    @Test
    fun `empty card number returns CardValidationError EMPTY_CARD_NUMBER`() {
        val cardName = "name"
        val cardNumber = ""
        val month= "12"
        val year = "14"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.EMPTY_CARD_NUMBER)
    }

    @Test
    fun `short card number returns CardValidationError INVALID_CARD_NUMBER`() {
        val cardName = "name"
        val cardNumber = "123412341234"
        val month= "12"
        val year = "14"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.INVALID_CARD_NUMBER)
    }

    @Test
    fun `empty month returns CardValidationError EMPTY_CARD_NUMBER`() {
        val cardName = "name"
        val cardNumber = "1234123412341234"
        val month= ""
        val year = "14"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.EMPTY_MONTH)
    }

    @Test
    fun `short month returns CardValidationError INVALID_MONTH`() {
        val cardName = "name"
        val cardNumber = "1234123412341234"
        val month= "1"
        val year = "14"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.INVALID_MONTH)
    }

    @Test
    fun `empty year returns CardValidationError EMPTY_YEAR`() {
        val cardName = "name"
        val cardNumber = "1234123412341234"
        val month= "12"
        val year = ""
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.EMPTY_YEAR)
    }

    @Test
    fun `short year returns CardValidationError INVALID_YEAR`() {
        val cardName = "name"
        val cardNumber = "1234123412341234"
        val month= "12"
        val year = "1"
        val cvv = "123"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.INVALID_YEAR)
    }

    @Test
    fun `empty cvv returns CardValidationError EMPTY_YEAR`() {
        val cardName = "name"
        val cardNumber = "1234123412341234"
        val month= "12"
        val year = "24"
        val cvv = ""

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.EMPTY_CVV)
    }

    @Test
    fun `short cvv returns CardValidationError INVALID_CVV`() {
        val cardName = "name"
        val cardNumber = "1234123412341234"
        val month= "12"
        val year = "24"
        val cvv = "1"

        val result = cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).error).isEqualTo(CardValidationError.INVALID_CVV)
    }
}