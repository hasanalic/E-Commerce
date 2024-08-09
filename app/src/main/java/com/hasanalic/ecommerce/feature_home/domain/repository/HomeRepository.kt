package com.hasanalic.ecommerce.feature_home.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.model.Product

interface HomeRepository {

    suspend fun getProductsByUserId(userId: String): Result<List<Product>, DataError.Local>

    suspend fun getCategories(): Result<List<Category>, DataError.Local>

    suspend fun getBrands(): Result<List<Brand>, DataError.Local>

    suspend fun getBrandsByCategory(category: String): Result<List<Brand>, DataError.Local>

    suspend fun getProductEntityIdByBarcode(productBarcode: String): Result<Int, DataError.Local>

    //suspend fun getFavoriteByProductId(userId: String, productId: String): Resource<Boolean>

    //suspend fun getShoppingCartByProductId(userId: String, productId: String): Resource<Boolean>
}