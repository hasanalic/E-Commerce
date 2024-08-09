package com.hasanalic.ecommerce.feature_filter.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity

interface FilterRepository {
    suspend fun getProductEntitiesBySearchQuery(searchQuery: String): Result<List<ProductEntity>, DataError.Local>

    suspend fun getProductEntitiesByCategory(productCategory: String): Result<List<ProductEntity>, DataError.Local>

    suspend fun getProductsByFilter(filter: Filter): Result<List<ProductEntity>, DataError.Local>
}