package com.hasanalic.ecommerce.feature_home.presentation.util

class TotalCost {

    companion object {
        fun calculateTotalCost(wholePart: Int, decimalPart: Int, quantity: Int = 1): Array<Int> {
            var totalWholePart = wholePart * quantity       // 3 * 2 = 6
            var totalDecimalPart = decimalPart * quantity   // 81 * 2 = 162
            totalWholePart += totalDecimalPart / 100        // 162 / 100 = 1    ---> 7
            totalDecimalPart %= 100                         // 162 % 100 = 62   ---> 62

            return arrayOf<Int>(totalWholePart, totalDecimalPart)
        }
    }
}