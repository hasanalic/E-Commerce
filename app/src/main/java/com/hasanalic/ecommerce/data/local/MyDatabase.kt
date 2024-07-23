package com.hasanalic.ecommerce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hasanalic.ecommerce.data.dto.AddressEntity
import com.hasanalic.ecommerce.data.dto.FavoritesEntity
import com.hasanalic.ecommerce.data.dto.NotificationEntity
import com.hasanalic.ecommerce.data.dto.OrderEntity
import com.hasanalic.ecommerce.data.dto.OrderProductsEntity
import com.hasanalic.ecommerce.data.dto.PaymentEntity
import com.hasanalic.ecommerce.data.dto.ProductEntity
import com.hasanalic.ecommerce.data.dto.ReviewEntity
import com.hasanalic.ecommerce.data.dto.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.data.dto.UserEntity

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