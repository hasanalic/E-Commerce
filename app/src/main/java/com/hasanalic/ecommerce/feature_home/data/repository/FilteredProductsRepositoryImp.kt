package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_home.domain.repository.FilteredProductsRepository
import javax.inject.Inject

class FilteredProductsRepositoryImp @Inject constructor(
    private val productDao: ProductDao
) : FilteredProductsRepository {
    override suspend fun getProductsByKeyword(
        userId: String,
        keyword: String
    ): Result<List<Product>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsByCategory(
        userId: String,
        category: String
    ): Result<List<Product>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsByFilter(
        userId: String,
        filter: Filter
    ): Result<List<Product>, DataError.Local> {
        TODO("Not yet implemented")
    }
}