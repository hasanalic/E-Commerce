package com.hasanalic.ecommerce.feature_shopping_cart.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_shopping_cart.domain.repository.ShoppingCartRepository
import javax.inject.Inject

class GetProductsInShoppingCartUseCase @Inject constructor(
    private val shoppingCartRepository: ShoppingCartRepository
) {
    suspend operator fun invoke(userId: String): Result<List<ShoppingCartItem>, DataError.Local> {
        return shoppingCartRepository.getProductsInShoppingCart(userId)
    }
}