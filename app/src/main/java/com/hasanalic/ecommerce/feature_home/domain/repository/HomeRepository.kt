package com.hasanalic.ecommerce.feature_home.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.Product

interface HomeRepository {
    suspend fun getProductsByUserId(userId: String): Result<List<Product>, DataError.Local>

    suspend fun getProductEntityIdByBarcode(productBarcode: String): Result<Int, DataError.Local>
}