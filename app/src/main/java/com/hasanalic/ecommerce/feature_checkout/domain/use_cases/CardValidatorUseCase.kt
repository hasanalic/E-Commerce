package com.hasanalic.ecommerce.feature_checkout.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.domain.model.CardValidationError

class CardValidatorUseCase {
    operator fun invoke(cardName: String, cardNumber: String, month: String, year: String, cvv: String): Result<Unit, CardValidationError> {
        if (cardName.isEmpty()) {
            return Result.Error(CardValidationError.EMPTY_CARD_NAME)
        }

        if (cardName.length < 4) {
            return Result.Error(CardValidationError.SHORT_CARD_NAME)
        }

        if (cardNumber.isEmpty()) {
            return Result.Error(CardValidationError.EMPTY_CARD_NUMBER)
        }

        if (cardNumber.length != 16) {
            return Result.Error(CardValidationError.INVALID_CARD_NUMBER)
        }

        if (month.isEmpty()) {
            return Result.Error(CardValidationError.EMPTY_MONTH)
        }

        if (month.length != 2) {
            return Result.Error(CardValidationError.INVALID_MONTH)
        }

        if (year.isEmpty()) {
            return Result.Error(CardValidationError.EMPTY_YEAR)
        }

        if (year.length != 2) {
            return Result.Error(CardValidationError.INVALID_YEAR)
        }

        if (cvv.isEmpty()) {
            return Result.Error(CardValidationError.EMPTY_CVV)
        }

        if (cvv.length != 3) {
            return Result.Error(CardValidationError.INVALID_CVV)
        }

        return Result.Success(Unit)
    }
}