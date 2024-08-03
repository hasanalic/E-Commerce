package com.hasanalic.ecommerce.feature_shopping_cart.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.domain.repository.ShoppingCartRepository
import javax.inject.Inject

class CheckShoppingCartEntityByProductIdUseCase @Inject constructor(
    private val shoppingCartRepository: ShoppingCartRepository
) {
    suspend operator fun invoke(userId: String, productId: String): Result<Boolean, DataError.Local> {
        return shoppingCartRepository.checkShoppingCartEntityByProductId(userId, productId)
    }
}