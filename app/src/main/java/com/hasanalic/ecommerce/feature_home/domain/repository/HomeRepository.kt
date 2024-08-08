package com.hasanalic.ecommerce.feature_home.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.utils.Resource

interface HomeRepository {

    suspend fun getProductsByUserId(userId: String): Result<List<Product>, DataError.Local>

    suspend fun getCategories(): Result<List<Category>, DataError.Local>

    suspend fun getBrands(): Result<List<Brand>, DataError.Local>

    suspend fun getBrandsByCategory(category: String): Result<List<Brand>, DataError.Local>

    suspend fun getProductEntityIdByBarcode(productBarcode: String): Result<Int, DataError.Local>

    // Db initialize
    suspend fun insertAllProductEntities(vararg products: ProductEntity): Result<Unit, DataError.Local>

    // Filter
    suspend fun getProductEntitiesBySearchQuery(searchQuery: String): Resource<List<ProductEntity>>

    suspend fun getProductEntitiesByCategory(productCategory: String): Resource<List<ProductEntity>>

    suspend fun getProductsByFilter(filter: Filter): Resource<List<ProductEntity>>



    //suspend fun getProductEntities(): Resource<List<ProductEntity>>

    //suspend fun getFavoriteByProductId(userId: String, productId: String): Resource<Boolean>

    //suspend fun getShoppingCartByProductId(userId: String, productId: String): Resource<Boolean>

    //suspend fun getReviewsByProductId(productId: String): Resource<List<ReviewEntity>>

    //suspend fun insertAllReviews(vararg reviews: ReviewEntity): Resource<Boolean>
}