package com.hasanalic.ecommerce.feature_orders.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity

data class OrderWithProductsDto(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderProductsId"
    )
    val products: List<OrderProductsEntity>
)