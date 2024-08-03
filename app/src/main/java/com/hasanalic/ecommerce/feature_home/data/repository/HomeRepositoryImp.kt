package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.feature_favorite.data.local.FavoritesDao
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_product_detail.data.local.ReviewDao
import com.hasanalic.ecommerce.feature_shopping_cart.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.data.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.model.Chip
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.feature_filter.presentation.Filter
import com.hasanalic.ecommerce.utils.Resource
import javax.inject.Inject
import kotlin.Exception

class HomeRepositoryImp @Inject constructor(
    private val favoritesDao: FavoritesDao,
    private val productDao: ProductDao,
    private val shoppingCartItemsDao: ShoppingCartItemsDao,
    private val reviewDao: ReviewDao
) : HomeRepository {

    override suspend fun getProducts(): Resource<List<ProductEntity>> {
        return try {
            val response = productDao.getProducts()
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"No Product Data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"error - getProducts")
        }
    }

    override suspend fun insertProducts(vararg products: ProductEntity): Resource<Boolean> {
        return try {
            val response = productDao.insertAllProducts(*products)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false, "Failed to insert products")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"insertProducts")
        }
    }

    override suspend fun getShoppingCartItems(userId: String): Resource<List<ShoppingCartItemsEntity>> {
        return try {
            val response = shoppingCartItemsDao.getShoppingCartItems(userId)
            response?.let {
                Resource.Success(it)
            } ?: Resource.Error(null,"No ShoppingCartItems data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"getShoppingCartItems")
        }
    }

    override suspend fun getShoppingCartCount(userId: String): Resource<Int> {
        return try {
            val response = shoppingCartItemsDao.getShoppingCartItemCount(userId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"No ShoppingCartItems data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"getShoppingCartCount")
        }
    }

    override suspend fun insertShoppingCartItems(shoppingCartItemsEntity: ShoppingCartItemsEntity): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.insertShoppingCartItemEntity(shoppingCartItemsEntity)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to insert shoppingCartItem")
            }
        } catch (e: Exception) {
            Resource.Error(false, e.localizedMessage?:"insertShoppingCartItems")
        }
    }

    override suspend fun insertAllShoppingCartItems(vararg shoppingCartItemsEntities: ShoppingCartItemsEntity): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.insertAllShoppingCartItemEntities(*shoppingCartItemsEntities)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false, "Failed to insert shopping cart item entity")
            }
        } catch (e: Exception) {
            Resource.Error(false, e.localizedMessage?:"insertAllShoppingCartItems")
        }
    }

    override suspend fun updateShoppingCartItem(
        userId: String,
        productId: String,
        quantity: String
    ): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.updateShoppingCartItemEntity(userId, productId,quantity.toInt())
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Error shoppingCartItem")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"updateShoppingCartItem")
        }
    }

    override suspend fun deleteShoppingCartItem(userId: String, productId: String): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.deleteShoppingCartItemEntity(userId, productId)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Error deleteShoppingCartItem")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"deleteShoppingCartItem")
        }
    }

    override suspend fun getFavorites(userId: String): Resource<List<FavoritesEntity>> {
        return try {
            val response = favoritesDao.getFavorites(userId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"List<FavoritesEntity> null")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"getFavorites")
        }
    }

    override suspend fun insertFavorite(favoritesEntity: FavoritesEntity): Resource<Boolean> {
        return try {
            val response = favoritesDao.insertFavorite(favoritesEntity)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to insert favorite")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"insertFavorite")
        }
    }

    override suspend fun deleteFavorite(userId: String, productId: String): Resource<Boolean> {
        return try {
            val response = favoritesDao.deleteFavorite(userId, productId)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to delete favorite")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"deleteFavorite")
        }
    }

    override suspend fun getFavoriteProducts(favoriteProductIds: List<String>): Resource<List<ProductEntity>> {
        return try {
            val response = productDao.getFavoriteProducts(favoriteProductIds)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch products")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"")
        }
    }

    override suspend fun getProductsBySearchQuery(searchQuery: String): Resource<List<ProductEntity>> {
        return try {
            val response = productDao.getProductsBySearchQuery(searchQuery)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch searched products")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"")
        }
    }

    override suspend fun getProductsByCategory(productCategory: String): Resource<List<ProductEntity>> {
        return try {
            val response = productDao.getProductsByCategory(productCategory)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch category filtered products")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"")
        }
    }

    override suspend fun getProductIdByBarcode(productBarcode: String): Resource<Int> {
        return try {
            val response = productDao.getProductByBarcode(productBarcode)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Ürün bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"")
        }
    }

    override suspend fun getProductById(productId: String): Resource<ProductEntity> {
        return try {
            val response = productDao.getProductById(productId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null, "Failed to fetch product")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"error getProductById")
        }
    }

    override suspend fun getReviewsByProductId(productId: String): Resource<List<ReviewEntity>> {
        return try {
            val response = reviewDao.getReviewsByProductId(productId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch reviews")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"getReviewsByProductId")
        }
    }

    override suspend fun insertAllReviews(vararg reviews: ReviewEntity): Resource<Boolean> {
        return try {
            val response = reviewDao.insertAllReviews(*reviews)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false, "Failed to insert reviews")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"insertAllReviews")
        }
    }

    override suspend fun getFavoriteByProductId(
        userId: String,
        productId: String
    ): Resource<Boolean> {
        return try {
            val response = favoritesDao.getFavoriteByProductId(userId, productId)
            response?.let {
                Resource.Success(true)
            }?: Resource.Success(false)
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"error getFavoriteByProductId")
        }
    }

    override suspend fun getShoppingCartByProductId(
        userId: String,
        productId: String
    ): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.getShoppingCartEntityByProductId(userId, productId)
            response?.let {
                Resource.Success(true)
            }?: Resource.Success(false)
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"error getFavoriteByProductId")
        }
    }

    override suspend fun getProductsByFilter(filter: Filter): Resource<List<ProductEntity>> {
        return try {
            val response = productDao.filterProducts(
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
    }

    override suspend fun getShoppingCartItemCount(userId: String): Resource<Int> {
        return try {
            val response = shoppingCartItemsDao.getShoppingCartItemCount(userId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null, "Failed to fetch ShoppingCartItemCount")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun getCategories(): Resource<List<Chip>> {
        return try {
            val response = productDao.getCategories()
            response?.let {categoryList->
                val uniqueCategories = categoryList.distinct()
                val chipCategoryList = uniqueCategories.map { Chip(it) }
                Resource.Success(chipCategoryList)
            }?: Resource.Error(null,"Kategori bulunamadı")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun getBrands(): Resource<List<Chip>> {
        return try {
            val response = productDao.getBrands()
            response?.let {brandList->
                val uniqueBrands = brandList.distinct()
                val chipBrandList = uniqueBrands.map { Chip(it) }
                Resource.Success(chipBrandList)
            }?: Resource.Error(null,"Marka bulunamadı")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }

    override suspend fun getBrandsByCategory(category: String): Resource<List<Chip>> {
        return try {
            val response = productDao.getBrandsByCategory(category)
            response?.let {brandList->
                val uniqueBrands = brandList.distinct()
                val chipBrandList = uniqueBrands.map { Chip(it) }
                Resource.Success(chipBrandList)
            }?: Resource.Error(null,"Marka bulunamadı")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Bilinmeyen bir hata meydana geldi")
        }
    }
}