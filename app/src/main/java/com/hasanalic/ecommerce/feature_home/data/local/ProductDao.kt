package com.hasanalic.ecommerce.feature_home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductDto
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Transaction
    @Query("""
        SELECT 
            p.*,
            CASE 
                WHEN c.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inCart,
            CASE 
                WHEN f.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inFavorite
        FROM Product p
        LEFT JOIN ShoppingCartItems c ON p.productId = c.product_id AND c.user_id = :userId
        LEFT JOIN Favorites f ON p.productId = f.product_id AND f.user_id = :userId
    """)
    suspend fun getProductsByUserId(userId: String): List<ProductDto>?

    @Query("""
        SELECT 
            p.*, 
            CASE WHEN sc.product_id IS NOT NULL THEN 1 ELSE 0 END AS inCart,
            CASE WHEN f.product_id IS NOT NULL THEN 1 ELSE 0 END AS inFavorite
        FROM Product p
        LEFT JOIN ShoppingCartItems sc ON p.productId = sc.product_id AND sc.user_id = :userId
        LEFT JOIN Favorites f ON p.productId = f.product_id AND f.user_id = :userId
        WHERE p.productId = :productId
    """)
    suspend fun getProductByUserIdAndProductId(userId: String, productId: String): ProductDto?

    @Transaction
    @Query("""
        SELECT 
            p.*,
            CASE 
                WHEN c.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inCart,
            CASE 
                WHEN f.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inFavorite
        FROM Product p
        LEFT JOIN ShoppingCartItems c ON p.productId = c.product_id AND c.user_id = :userId
        LEFT JOIN Favorites f ON p.productId = f.product_id AND f.user_id = :userId
        WHERE p.product_brand LIKE '%' || :keyword || '%'
        OR p.product_detail LIKE '%' || :keyword || '%'
    """)
    suspend fun getProductsByKeyword(userId: String, keyword: String): List<ProductDto>?

    @Transaction
    @Query("""
        SELECT 
            p.*,
            CASE 
                WHEN c.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inCart,
            CASE 
                WHEN f.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inFavorite
        FROM Product p
        LEFT JOIN ShoppingCartItems c ON p.productId = c.product_id AND c.user_id = :userId
        LEFT JOIN Favorites f ON p.productId = f.product_id AND f.user_id = :userId
        WHERE (:category IS NULL OR p.product_category = :category)
        AND (:brand IS NULL OR p.product_brand = :brand)
        AND (:minPrice IS NULL OR product_price_whole >= :minPrice)
        AND (:maxPrice IS NULL OR product_price_whole <= :maxPrice)
        AND (:minStar IS NULL OR p.product_rate >= :minStar)
        AND (:maxStar IS NULL OR p.product_rate <= :maxStar)
    """)
    suspend fun filterProductsByCriteria(
        userId: String,
        category: String?,
        brand: String?,
        minPrice: Int?,
        maxPrice: Int?,
        minStar: Double?,
        maxStar: Double?
    ): List<ProductDto>?


    @Transaction
    @Query("""
        SELECT 
            p.*,
            CASE 
                WHEN c.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inCart,
            CASE 
                WHEN f.product_id IS NOT NULL THEN 1 
                ELSE 0 
            END AS inFavorite
        FROM Product p
        LEFT JOIN ShoppingCartItems c ON p.productId = c.product_id AND c.user_id = :userId
        LEFT JOIN Favorites f ON p.productId = f.product_id AND f.user_id = :userId
        WHERE p.product_category = :category 
    """)
    suspend fun getProductsByCategory(userId: String, category: String): List<ProductDto>?

    @Query("SELECT productId FROM Product WHERE product_barcode = :productBarcode")
    suspend fun getProductEntityIdByBarcode(productBarcode: String): Int?

    @Query("SELECT product_category FROM Product")
    suspend fun getCategories(): List<String>?

    @Query("SELECT product_brand FROM Product WHERE product_category = :productCategory")
    suspend fun getBrandsByCategory(productCategory: String): List<String>?

    @Query("SELECT product_brand FROM Product")
    suspend fun getBrands(): List<String>?

    @Insert
    suspend fun insertAllProductEntities(vararg products: ProductEntity): List<Long>
}