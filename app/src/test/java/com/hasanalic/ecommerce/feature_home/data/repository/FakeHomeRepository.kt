package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.utils.Resource

class FakeHomeRepository : HomeRepository {

    private val products = mutableListOf(
        Product("1","category","photo",
            "brand","detail", 1,
            1,1.0, "1",
            "123456789",false,false,false)
    )

    override suspend fun getProductsByUserId(userId: String): Result<List<Product>, DataError.Local> {
        return Result.Success(products)
    }

    override suspend fun getProductEntityIdByBarcode(productBarcode: String): Result<Int, DataError.Local> {
        TODO("Not yet implemented")
    }
}