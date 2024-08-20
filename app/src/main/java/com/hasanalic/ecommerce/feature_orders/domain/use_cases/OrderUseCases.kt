package com.hasanalic.ecommerce.feature_orders.domain.use_cases

data class OrderUseCases(
    val getOrderDetailUseCase: GetOrderDetailUseCase,
    val getOrdersByUserUseCase: GetOrdersByUserUseCase,
    val insertOrderUseCase: InsertOrderUseCase,
    val updateOrderStatusUseCase: UpdateOrderStatusUseCase
)