package com.hasanalic.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.hasanalic.ecommerce.data.local.AddressDao
import com.hasanalic.ecommerce.data.local.FavoritesDao
import com.hasanalic.ecommerce.data.local.MyDatabase
import com.hasanalic.ecommerce.data.local.NotificationDao
import com.hasanalic.ecommerce.data.local.OrderDao
import com.hasanalic.ecommerce.data.local.OrderProductsDao
import com.hasanalic.ecommerce.data.local.PaymentDao
import com.hasanalic.ecommerce.data.local.ProductDao
import com.hasanalic.ecommerce.data.local.ReviewDao
import com.hasanalic.ecommerce.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.data.repository.CheckoutRepositoryImp
import com.hasanalic.ecommerce.data.repository.HomeRepositoryImp
import com.hasanalic.ecommerce.data.repository.OrderRepositoryImp
import com.hasanalic.ecommerce.data.repository.ServiceRepositoryImp
import com.hasanalic.ecommerce.domain.repository.CheckoutRepository
import com.hasanalic.ecommerce.domain.repository.HomeRepository
import com.hasanalic.ecommerce.domain.repository.OrderRepository
import com.hasanalic.ecommerce.domain.repository.ServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MyDatabase::class.java, "MyDatabase"
    ).build()

    @Singleton
    @Provides
    fun provideFavoritesDao(database: MyDatabase) = database.favoritesDao()

    @Singleton
    @Provides
    fun provideOrderDao(database: MyDatabase) = database.orderDao()

    @Singleton
    @Provides
    fun provideProductDao(database: MyDatabase) = database.productDao()

    @Singleton
    @Provides
    fun provideShoppingCartItemsDao(database: MyDatabase) = database.shoppingCartItemsDao()

    @Singleton
    @Provides
    fun provideUserDao(database: MyDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideReviewDao(database: MyDatabase) = database.reviewDao()

    @Singleton
    @Provides
    fun provideOrderProductsDao(database: MyDatabase) = database.orderProductsDao()

    @Singleton
    @Provides
    fun provideAddressDao(database: MyDatabase) = database.addressDao()

    @Singleton
    @Provides
    fun providePaymentDao(database: MyDatabase) = database.paymentDao()

    @Singleton
    @Provides
    fun provideNotificationDao(database: MyDatabase) = database.notificationDao()

    @Singleton
    @Provides
    fun provideHomeRepository(
        favoritesDao: FavoritesDao,
        productDao: ProductDao,
        shoppingCartItemsDao: ShoppingCartItemsDao,
        reviewDao: ReviewDao
    ): HomeRepository {
        return HomeRepositoryImp(favoritesDao, productDao, shoppingCartItemsDao, reviewDao)
    }

    @Singleton
    @Provides
    fun provideCheckoutRepository(
        addressDao: AddressDao,
        orderDao: OrderDao,
        orderProductsDao: OrderProductsDao,
        paymentDao: PaymentDao,
        shoppingCartItemsDao: ShoppingCartItemsDao
    ): CheckoutRepository {
        return CheckoutRepositoryImp(
            addressDao,
            orderDao,
            orderProductsDao,
            paymentDao,
            shoppingCartItemsDao
        )
    }

    @Singleton
    @Provides
    fun provideOrderRepository(
        addressDao: AddressDao,
        orderDao: OrderDao,
        orderProductsDao: OrderProductsDao,
        paymentDao: PaymentDao
    ): OrderRepository {
        return OrderRepositoryImp(
            addressDao, orderDao, orderProductsDao, paymentDao
        )
    }

    @Singleton
    @Provides
    fun provideServiceRepository(
        shoppingCartItemsDao: ShoppingCartItemsDao,
        notificationDao: NotificationDao
    ): ServiceRepository {
        return ServiceRepositoryImp(
            shoppingCartItemsDao, notificationDao
        )
    }
}