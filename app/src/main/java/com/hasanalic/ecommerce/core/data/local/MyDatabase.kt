package com.hasanalic.ecommerce.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hasanalic.ecommerce.feature_location.data.entity.AddressEntity
import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_notification.data.entity.NotificationEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_checkout.data.entity.PaymentEntity
import com.hasanalic.ecommerce.feature_home.data.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import com.hasanalic.ecommerce.feature_auth.data.local.UserDao
import com.hasanalic.ecommerce.feature_checkout.data.local.PaymentDao
import com.hasanalic.ecommerce.feature_favorite.data.local.FavoritesDao
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_location.data.local.AddressDao
import com.hasanalic.ecommerce.feature_notification.data.local.NotificationDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderProductsDao
import com.hasanalic.ecommerce.feature_product_detail.data.local.ReviewDao
import com.hasanalic.ecommerce.feature_shopping_cart.data.local.ShoppingCartItemsDao

@Database(entities = [UserEntity::class, FavoritesEntity::class, OrderEntity::class,
    OrderProductsEntity::class, ProductEntity::class, ShoppingCartItemsEntity::class,
    ReviewEntity::class, AddressEntity::class, PaymentEntity::class, NotificationEntity::class], version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun orderDao(): OrderDao
    abstract fun orderProductsDao(): OrderProductsDao
    abstract fun productDao(): ProductDao
    abstract fun shoppingCartItemsDao(): ShoppingCartItemsDao
    abstract fun reviewDao(): ReviewDao
    abstract fun addressDao(): AddressDao
    abstract fun paymentDao(): PaymentDao
    abstract fun notificationDao(): NotificationDao
}