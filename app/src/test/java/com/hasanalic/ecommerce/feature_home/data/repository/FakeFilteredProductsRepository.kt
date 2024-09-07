package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_home.domain.repository.FilteredProductsRepository

class FakeFilteredProductsRepository : FilteredProductsRepository {
    private val products = mutableListOf(
        Product("1","category","photo",
            "brand","detail", 1,
            1,1.0, "1",
            "123456789",false,false)
    )

    override suspend fun getProductsByKeyword(
        userId: String,
        keyword: String
    ): Result<List<Product>, DataError.Local> {
        return Result.Success(products)
    }

    override suspend fun getProductsByCategory(
        userId: String,
        category: String
    ): Result<List<Product>, DataError.Local> {
        return Result.Success(products)
    }

    override suspend fun getProductsByFilter(
        userId: String,
        filter: Filter
    ): Result<List<Product>, DataError.Local> {
        return Result.Success(products)
    }
}