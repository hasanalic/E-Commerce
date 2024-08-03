package com.hasanalic.ecommerce.feature_shopping_cart.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_shopping_cart.domain.repository.ShoppingCartRepository
import javax.inject.Inject

class InsertShoppingCartItemEntityUseCase @Inject constructor(
    private val shoppingCartRepository: ShoppingCartRepository
) {
    suspend operator fun invoke(shoppingCartItemsEntity: ShoppingCartItemsEntity): Result<Unit, DataError.Local> {
        return shoppingCartRepository.insertShoppingCartItemEntity(shoppingCartItemsEntity)
    }
}