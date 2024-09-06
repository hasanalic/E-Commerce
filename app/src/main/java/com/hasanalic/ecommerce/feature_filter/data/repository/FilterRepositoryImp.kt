package com.hasanalic.ecommerce.feature_filter.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import javax.inject.Inject

class FilterRepositoryImp @Inject constructor(
    private val productDao: ProductDao
) : FilterRepository {
    override suspend fun getCategories(): Result<List<Category>, DataError.Local> {
        return try {
            val result = productDao.getCategories()
            result?.let { categoryList->
                val uniqueCategories = categoryList.distinct()
                val chipCategoryList = uniqueCategories.map { Category(it) }
                Result.Success(chipCategoryList)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getBrands(): Result<List<Brand>, DataError.Local> {
        return try {
            val result = productDao.getBrands()
            result?.let { brandList->
                val uniqueBrands = brandList.distinct()
                val chipBrandList = uniqueBrands.map { Brand(it) }
                Result.Success(chipBrandList)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getBrandsByCategory(category: String): Result<List<Brand>, DataError.Local> {
        return try {
            val result = productDao.getBrandsByCategory(category)
            result?.let { brandList->
                val uniqueBrands = brandList.distinct()
                val chipBrandList = uniqueBrands.map { Brand(it) }
                Result.Success(chipBrandList)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}