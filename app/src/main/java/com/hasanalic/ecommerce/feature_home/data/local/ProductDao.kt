package com.hasanalic.ecommerce.feature_home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    suspend fun getProducts(): List<ProductEntity>?

    @Query("SELECT * FROM Product WHERE productId = :productId")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("SELECT * FROM Product WHERE product_brand LIKE '%' || :searchQuery || '%' OR product_detail LIKE '%' || :searchQuery || '%'")
    suspend fun getProductsBySearchQuery(searchQuery: String): List<ProductEntity>?

    @Query("SELECT * FROM Product WHERE product_category = :productCategory")
    suspend fun getProductsByCategory(productCategory: String): List<ProductEntity>?

    @Query("SELECT productId FROM Product WHERE product_barcode = :productBarcode")
    suspend fun getProductByBarcode(productBarcode: String): Int?

    @Query("SELECT product_category FROM Product")
    suspend fun getCategories(): List<String>?

    @Query("SELECT product_brand FROM Product WHERE product_category = :productCategory")
    suspend fun getBrandsByCategory(productCategory: String): List<String>?

    @Query("SELECT product_brand FROM Product")
    suspend fun getBrands(): List<String>?

    @Query("""
        SELECT p.* FROM Product p
        INNER JOIN Favorites f ON p.productId = f.product_id
        WHERE f.user_id = :userId
    """)
    suspend fun getFavoriteProductsByUserId(userId: String): List<ProductEntity>

    @Insert
    suspend fun insertAllProducts(vararg products: ProductEntity): List<Long>

    @Query("SELECT * FROM Product WHERE productId IN (:favoriteProductIds)")
    suspend fun getFavoriteProducts(favoriteProductIds: List<String>): List<ProductEntity>?

    @Query("SELECT * FROM Product WHERE " +
            "(:category IS NULL OR product_category = :category) " +
            "AND (:brand IS NULL OR product_brand = :brand) " +
            "AND (:minPrice IS NULL OR product_price_whole >= :minPrice) " +
            "AND (:maxPrice IS NULL OR product_price_whole <= :maxPrice) " +
            "AND (:minStar IS NULL OR product_rate >= :minStar) " +
            "AND (:maxStar IS NULL OR product_rate <= :maxStar)")
    suspend fun filterProducts(category: String?, brand: String?, minPrice: Int?, maxPrice: Int?, minStar: Double?, maxStar: Double?): List<ProductEntity>?
}