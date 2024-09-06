package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository

class FakeHomeRepository : HomeRepository {

    private val products = mutableListOf(
        Product("1","category","photo",
            "brand","detail", 1,
            1,1.0, "1",
            "123456789",false,false)
    )

    override suspend fun getProductsByUserId(userId: String): Result<List<Product>, DataError.Local> {
        return Result.Success(products)
    }

    override suspend fun getProductEntityIdByBarcode(productBarcode: String): Result<Int, DataError.Local> {
        return Result.Success(1)
    }
}