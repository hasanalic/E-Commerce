package com.hasanalic.ecommerce.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import com.hasanalic.ecommerce.feature_auth.data.local.UserDao
import com.hasanalic.ecommerce.feature_checkout.data.local.CardDao
import com.hasanalic.ecommerce.feature_home.data.local.FavoritesDao
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_location.data.local.AddressDao
import com.hasanalic.ecommerce.feature_notification.data.local.NotificationDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_checkout.data.local.OrderProductsDao
import com.hasanalic.ecommerce.feature_product_detail.data.local.ReviewDao
import com.hasanalic.ecommerce.feature_home.data.local.ShoppingCartItemsDao

@Database(entities = [UserEntity::class, FavoritesEntity::class, OrderEntity::class,
    OrderProductsEntity::class, ProductEntity::class, ShoppingCartItemsEntity::class,
    ReviewEntity::class, AddressEntity::class, CardEntity::class, NotificationEntity::class], version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun orderDao(): OrderDao
    abstract fun orderProductsDao(): OrderProductsDao
    abstract fun productDao(): ProductDao
    abstract fun shoppingCartItemsDao(): ShoppingCartItemsDao
    abstract fun reviewDao(): ReviewDao
    abstract fun addressDao(): AddressDao
    abstract fun cardDao(): CardDao
    abstract fun notificationDao(): NotificationDao
}