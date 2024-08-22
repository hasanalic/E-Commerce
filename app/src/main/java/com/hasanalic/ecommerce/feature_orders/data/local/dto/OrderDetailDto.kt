package com.hasanalic.ecommerce.feature_orders.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity

data class OrderDetailDto(
    @Embedded val order: OrderEntity,

    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderProductsId"
    )
    val products: List<OrderProductsEntity>,

    @Relation(
        parentColumn = "order_address_id",
        entityColumn = "addressId"
    )
    val address: AddressEntity,

    @Relation(
        parentColumn = "order_card_id",
        entityColumn = "cardId"
    )
    val card: CardEntity?
)