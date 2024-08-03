package com.hasanalic.ecommerce.feature_shopping_cart.domain.use_cases

data class ShoppingCartUseCases(
    val checkShoppingCartEntityByProductIdUseCase: CheckShoppingCartEntityByProductIdUseCase,
    val deleteShoppingCartItemEntitiesByProductIdListUseCase: DeleteShoppingCartItemEntitiesByProductIdListUseCase,
    val deleteShoppingCartItemEntityUseCase: DeleteShoppingCartItemEntityUseCase,
    val getProductsInShoppingCartUseCase: GetProductsInShoppingCartUseCase,
    val getShoppingCartItemCountUseCase: GetShoppingCartItemCountUseCase,
    val insertAllShoppingCartItemEntitiesUseCase: InsertAllShoppingCartItemEntitiesUseCase,
    val insertShoppingCartItemEntityUseCase: InsertShoppingCartItemEntityUseCase,
    val updateShoppingCartItemEntityUseCase: UpdateShoppingCartItemEntityUseCase
)