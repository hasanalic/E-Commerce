package com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases

data class ShoppingCartUseCases(
    val checkIfProductInCartUseCase: CheckIfProductInCartUseCase,
    val deleteShoppingCartItemEntitiesByProductIdListUseCase: DeleteShoppingCartItemEntitiesByProductIdListUseCase,
    val deleteShoppingCartItemEntityUseCase: DeleteShoppingCartItemEntityUseCase,
    val getProductsInShoppingCartUseCase: GetProductsInShoppingCartUseCase,
    val getShoppingCartItemCountUseCase: GetShoppingCartItemCountUseCase,
    val insertAllShoppingCartItemEntitiesUseCase: InsertAllShoppingCartItemEntitiesUseCase,
    val insertShoppingCartItemEntityUseCase: InsertShoppingCartItemEntityUseCase,
    val updateShoppingCartItemEntityUseCase: UpdateShoppingCartItemEntityUseCase
)