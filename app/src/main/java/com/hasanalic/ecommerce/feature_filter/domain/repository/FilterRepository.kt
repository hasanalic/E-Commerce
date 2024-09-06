package com.hasanalic.ecommerce.feature_filter.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category

interface FilterRepository {
    suspend fun getCategories(): Result<List<Category>, DataError.Local>

    suspend fun getBrands(): Result<List<Brand>, DataError.Local>

    suspend fun getBrandsByCategory(category: String): Result<List<Brand>, DataError.Local>
}