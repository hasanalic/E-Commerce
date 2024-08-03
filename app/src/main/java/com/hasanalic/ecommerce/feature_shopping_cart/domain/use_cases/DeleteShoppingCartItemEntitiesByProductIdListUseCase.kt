package com.hasanalic.ecommerce.feature_shopping_cart.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_shopping_cart.domain.repository.ShoppingCartRepository
import javax.inject.Inject

class DeleteShoppingCartItemEntitiesByProductIdListUseCase @Inject constructor(
    private val shoppingCartRepository: ShoppingCartRepository
) {
    suspend operator fun invoke(userId: String, productIds: List<String>): Result<Unit, DataError.Local> {
        return shoppingCartRepository.deleteShoppingCartItemEntitiesByProductIdList(userId, productIds)
    }
}