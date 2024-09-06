package com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import javax.inject.Inject

class CheckIfProductInCartUseCase @Inject constructor(
    private val shoppingCartRepository: ShoppingCartRepository
) {
    suspend operator fun invoke(userId: String, productId: String): Result<Boolean, DataError.Local> {
        return shoppingCartRepository.checkIfProductInCart(userId, productId)
    }
}