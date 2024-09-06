package com.hasanalic.ecommerce.feature_home.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.domain.model.Product

interface FilteredProductsRepository {

    suspend fun getProductsByKeyword(userId: String, keyword: String): Result<List<Product>, DataError.Local>

    suspend fun getProductsByCategory(userId: String, category: String): Result<List<Product>, DataError.Local>

    suspend fun getProductsByFilter(userId: String, filter: Filter): Result<List<Product>, DataError.Local>
}