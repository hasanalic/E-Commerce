package com.hasanalic.ecommerce.utils

import java.util.concurrent.TimeUnit

class OrderStatus {
    companion object {
        fun getOrderCurrrentStatus(orderTimestamp: Long, currentTimeStamp: Long): String {
            val differenceInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTimeStamp - orderTimestamp)
            val differenceInMinutes = differenceInSeconds / 60

            if (differenceInMinutes < 2L) {
                return Constants.ORDER_RECEIVED
            } else if (differenceInMinutes in 5L downTo 2L) {
                return Constants.ORDER_PREPARE
            } else if (differenceInMinutes in 20L downTo 5L) {
                return Constants.ORDER_CARGO
            } else {
                return Constants.ORDER_DELIVERED
            }
        }
    }
}