package com.hasanalic.ecommerce.feature_orders.presentation.utils

import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_CARGO
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_DELIVERED
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_PREPARE
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_RECEIVED
import java.util.concurrent.TimeUnit

class OrderStatus {
    companion object {
        fun getOrderCurrrentStatus(orderTimestamp: Long, currentTimeStamp: Long): String {
            val differenceInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTimeStamp - orderTimestamp)
            val differenceInMinutes = differenceInSeconds / 60

            if (differenceInMinutes < 2L) {
                return ORDER_RECEIVED
            } else if (differenceInMinutes in 5L downTo 2L) {
                return ORDER_PREPARE
            } else if (differenceInMinutes in 20L downTo 5L) {
                return ORDER_CARGO
            } else {
                return ORDER_DELIVERED
            }
        }
    }
}