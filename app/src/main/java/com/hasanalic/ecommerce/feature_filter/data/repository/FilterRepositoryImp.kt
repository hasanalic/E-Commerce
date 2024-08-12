package com.hasanalic.ecommerce.feature_filter.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
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

    override suspend fun getProductEntitiesBySearchQuery(searchQuery: String): Result<List<ProductEntity>, DataError.Local> {
        /*
        return try {
            val response = productDao.getProductEntitiesBySearchQuery(searchQuery)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch searched products")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"")
        }
         */
        return Result.Success(listOf())
    }

    override suspend fun getProductEntitiesByCategory(productCategory: String): Result<List<ProductEntity>, DataError.Local> {
        /*
        return try {
            val response = productDao.getProductEntitiesByCategory(productCategory)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch category filtered products")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"")
        }
         */
        return Result.Success(listOf())
    }

    override suspend fun getProductsByFilter(filter: Filter): Result<List<ProductEntity>, DataError.Local> {
        /*
        return try {
            val response = productDao.filterProductEntities(
                category = filter.category,
                brand = filter.brand,
                minPrice = filter.minPrice,
                maxPrice = filter.maxPrice,
                minStar = filter.minStar,
                maxStar = filter.maxStar
            )
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch product")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
         */
        return Result.Success(listOf())
    }
}