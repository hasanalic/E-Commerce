package com.hasanalic.ecommerce.feature_filter.data

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category

class FakeFilterRepository : FilterRepository {

    private val categories = mutableListOf(
        Category("category"), Category("category2")
    )

    private val brands = mutableListOf(
        Brand("brand"), Brand("brand2")
    )

    override suspend fun getCategories(): Result<List<Category>, DataError.Local> {
        return Result.Success(categories)
    }

    override suspend fun getBrands(): Result<List<Brand>, DataError.Local> {
        return Result.Success(brands)
    }

    override suspend fun getBrandsByCategory(category: String): Result<List<Brand>, DataError.Local> {
        return Result.Success(brands)
    }

    override suspend fun getProductEntitiesBySearchQuery(searchQuery: String): Result<List<ProductEntity>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductEntitiesByCategory(productCategory: String): Result<List<ProductEntity>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsByFilter(filter: Filter): Result<List<ProductEntity>, DataError.Local> {
        TODO("Not yet implemented")
    }
}