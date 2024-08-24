package com.hasanalic.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.hasanalic.ecommerce.feature_location.data.local.AddressDao
import com.hasanalic.ecommerce.feature_home.data.local.FavoritesDao
import com.hasanalic.ecommerce.core.data.local.MyDatabase
import com.hasanalic.ecommerce.feature_notification.data.local.NotificationDao
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_checkout.data.local.CardDao
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_product_detail.data.local.ReviewDao
import com.hasanalic.ecommerce.feature_home.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.feature_home.data.repository.HomeRepositoryImp
import com.hasanalic.ecommerce.feature_orders.data.repository.OrderRepositoryImp
import com.hasanalic.ecommerce.core.data.repository.ServiceRepositoryImp
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import com.hasanalic.ecommerce.core.domain.repository.ServiceRepository
import com.hasanalic.ecommerce.feature_auth.data.local.UserDao
import com.hasanalic.ecommerce.feature_auth.data.repository.AuthenticationRepositoryImp
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.GetUserByEmailAndPassUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.InsertUserUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserEmailValidatorUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserInputValidatorUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserPasswordValidatorUseCase
import com.hasanalic.ecommerce.feature_checkout.data.local.OrderProductsDao
import com.hasanalic.ecommerce.feature_checkout.data.repository.CardRepositoryImp
import com.hasanalic.ecommerce.feature_checkout.data.repository.OrderProductsRepositoryImp
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.repository.OrderProductsRepository
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardValidatorUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardByUserIdAndCardIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardsByUserIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertCardEntityUseCase
import com.hasanalic.ecommerce.feature_filter.data.repository.FilterRepositoryImp
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.FilterUseCases
import com.hasanalic.ecommerce.feature_home.data.repository.FavoriteRepositoryImp
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.DeleteFavoriteUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteIdByUserIdAndProductIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteListByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.InsertFavoriteAndGetIdUseCase
import com.hasanalic.ecommerce.feature_home.data.repository.ShoppingCartRepositoryImp
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsByCategoryUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetCategoriesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.GetProductEntityIdByBarcodeUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.GetProductsByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.HomeUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.CheckShoppingCartEntityByProductIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.DeleteShoppingCartItemEntitiesByProductIdListUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.DeleteShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.GetProductsInShoppingCartUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.GetShoppingCartItemCountUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.InsertAllShoppingCartItemEntitiesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.InsertShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.UpdateShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_location.data.repository.AddressRepositoryImp
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressUseCases
import com.hasanalic.ecommerce.feature_location.domain.use_cases.AddressValidatorUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.DeleteUserAddressUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetAddressEntityByUserIdAndAddressIdUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetAddressEntityListByUserIdUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.GetAddressListByUserIdUseCase
import com.hasanalic.ecommerce.feature_location.domain.use_cases.InsertAddressEntityUseCase
import com.hasanalic.ecommerce.feature_notification.data.repository.NotificationRepositoryImp
import com.hasanalic.ecommerce.feature_notification.domain.repository.NotificationRepository
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.GetUserNotificationsUseCase
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.InsertNotificationEntityUseCase
import com.hasanalic.ecommerce.feature_notification.domain.use_cases.NotificationUseCases
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrderDetailUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.GetOrdersByUserUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.InsertOrderUseCase
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.UpdateOrderStatusUseCase
import com.hasanalic.ecommerce.feature_product_detail.data.repository.ProductDetailRepositoryImp
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository
import com.hasanalic.ecommerce.feature_product_detail.domain.use_cases.GetProductDetailByUserIdAndProductIdUseCase
import com.hasanalic.ecommerce.feature_product_detail.domain.use_cases.GetReviewsByProductIdUseCase
import com.hasanalic.ecommerce.feature_product_detail.domain.use_cases.ProductDetailUseCases
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
    fun provideCardDao(database: MyDatabase) = database.cardDao()

    @Singleton
    @Provides
    fun provideNotificationDao(database: MyDatabase) = database.notificationDao()

    @Singleton
    @Provides
    fun provideHomeRepository(
        productDao: ProductDao,
    ): HomeRepository {
        return HomeRepositoryImp(productDao)
    }

    @Singleton
    @Provides
    fun provideOrderRepository(orderDao: OrderDao): OrderRepository {
        return OrderRepositoryImp(
           orderDao
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

    @Singleton
    @Provides
    fun provideAuthenticationRepository(
        userDao: UserDao
    ): AuthenticationRepository {
        return AuthenticationRepositoryImp(
            userDao
        )
    }

    @Singleton
    @Provides
    fun provideFavoriteRepository(favoriteDao: FavoritesDao): FavoriteRepository {
        return FavoriteRepositoryImp(
            favoritesDao = favoriteDao
        )
    }

    @Singleton
    @Provides
    fun provideShoppingCartRepository(shoppingCartItemsDao: ShoppingCartItemsDao): ShoppingCartRepository {
        return ShoppingCartRepositoryImp(shoppingCartItemsDao)
    }

    @Singleton
    @Provides
    fun provideProductDetailRepository(productDao: ProductDao, reviewDao: ReviewDao): ProductDetailRepository {
        return ProductDetailRepositoryImp(productDao, reviewDao)
    }

    @Singleton
    @Provides
    fun provideAddressRepository(addressDao: AddressDao): AddressRepository {
        return AddressRepositoryImp(addressDao)
    }

    @Singleton
    @Provides
    fun provideFilterRepository(productDao: ProductDao): FilterRepository {
        return FilterRepositoryImp(productDao)
    }

    @Singleton
    @Provides
    fun provideNotificationRepository(notificationDao: NotificationDao): NotificationRepository {
        return NotificationRepositoryImp(notificationDao)
    }

    @Singleton
    @Provides
    fun provideCardRepository(cardDao: CardDao): CardRepository {
        return CardRepositoryImp(cardDao)
    }

    @Singleton
    @Provides
    fun provideOrderProductsRepository(orderProductsDao: OrderProductsDao): OrderProductsRepository {
        return OrderProductsRepositoryImp(orderProductsDao)
    }

    @Singleton
    @Provides
    fun provideAuthUseCases(authRepository: AuthenticationRepository): AuthUseCases {
        return AuthUseCases(
            insertUserUseCase = InsertUserUseCase(authRepository),
            getUserByEmailAndPassUseCase = GetUserByEmailAndPassUseCase(authRepository),
            userEmailValidatorUseCase = UserEmailValidatorUseCase(),
            userInputValidatorUseCase = UserInputValidatorUseCase(),
            userPasswordValidatorUseCase = UserPasswordValidatorUseCase()
        )
    }

    @Singleton
    @Provides
    fun provideFavoriteUseCases(favoriteRepository: FavoriteRepository): FavoriteUseCases {
        return FavoriteUseCases(
            deleteFavoriteUseCase = DeleteFavoriteUseCase(favoriteRepository),
            getFavoriteIdByUserIdAndProductIdUseCase = GetFavoriteIdByUserIdAndProductIdUseCase(favoriteRepository),
            getFavoriteListByUserIdUseCase = GetFavoriteListByUserIdUseCase(favoriteRepository),
            getFavoriteProductsUseCase = GetFavoriteProductsUseCase(favoriteRepository),
            insertFavoriteAndGetIdUseCase = InsertFavoriteAndGetIdUseCase(favoriteRepository)
        )
    }

    @Singleton
    @Provides
    fun provideShoppingCartUseCases(shoppingCartRepository: ShoppingCartRepository): ShoppingCartUseCases {
        return ShoppingCartUseCases(
            checkShoppingCartEntityByProductIdUseCase = CheckShoppingCartEntityByProductIdUseCase(shoppingCartRepository),
            deleteShoppingCartItemEntitiesByProductIdListUseCase = DeleteShoppingCartItemEntitiesByProductIdListUseCase(shoppingCartRepository),
            deleteShoppingCartItemEntityUseCase = DeleteShoppingCartItemEntityUseCase(shoppingCartRepository),
            getProductsInShoppingCartUseCase = GetProductsInShoppingCartUseCase(shoppingCartRepository),
            getShoppingCartItemCountUseCase = GetShoppingCartItemCountUseCase(shoppingCartRepository),
            insertAllShoppingCartItemEntitiesUseCase = InsertAllShoppingCartItemEntitiesUseCase(shoppingCartRepository),
            insertShoppingCartItemEntityUseCase = InsertShoppingCartItemEntityUseCase(shoppingCartRepository),
            updateShoppingCartItemEntityUseCase = UpdateShoppingCartItemEntityUseCase(shoppingCartRepository)
        )
    }

    @Singleton
    @Provides
    fun provideHomeUseCases(homeRepository: HomeRepository): HomeUseCases {
        return HomeUseCases(
            getProductsByUserIdUseCase = GetProductsByUserIdUseCase(homeRepository),
            getProductEntityIdByBarcodeUseCase = GetProductEntityIdByBarcodeUseCase(homeRepository)
        )
    }

    @Singleton
    @Provides
    fun provideAddressUseCases(addressRepository: AddressRepository): AddressUseCases {
        return AddressUseCases(
            deleteUserAddressUseCase = DeleteUserAddressUseCase(addressRepository),
            getAddressEntityByUserIdAndAddressIdUseCase = GetAddressEntityByUserIdAndAddressIdUseCase(addressRepository),
            getAddressEntityListByUserIdUseCase = GetAddressEntityListByUserIdUseCase(addressRepository),
            getAddressListByUserIdUseCase = GetAddressListByUserIdUseCase(addressRepository),
            insertAddressEntityUseCase = InsertAddressEntityUseCase(addressRepository),
            addressValidatorUseCase = AddressValidatorUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideFilterUseCases(filterRepository: FilterRepository): FilterUseCases {
        return FilterUseCases(
            getBrandsUseCase = GetBrandsUseCase(filterRepository),
            getBrandsByCategoryUseCase = GetBrandsByCategoryUseCase(filterRepository),
            getCategoriesUseCase = GetCategoriesUseCase(filterRepository)
        )
    }

    @Singleton
    @Provides
    fun provideProductDetailUseCases(productDetailRepository: ProductDetailRepository): ProductDetailUseCases {
        return ProductDetailUseCases(
            getProductDetailByUserIdAndProductIdUseCase = GetProductDetailByUserIdAndProductIdUseCase(productDetailRepository),
            getReviewsByProductIdUseCase = GetReviewsByProductIdUseCase(productDetailRepository)
        )
    }

    @Singleton
    @Provides
    fun provideNotificationUseCases(notificationRepository: NotificationRepository): NotificationUseCases {
        return NotificationUseCases(
            getUserNotificationsUseCase = GetUserNotificationsUseCase(notificationRepository),
            insertNotificationEntityUseCase = InsertNotificationEntityUseCase(notificationRepository)
        )
    }

    @Singleton
    @Provides
    fun provideOrderUseCases(orderRepository: OrderRepository): OrderUseCases {
        return OrderUseCases(
            getOrderDetailUseCase = GetOrderDetailUseCase(orderRepository),
            getOrdersByUserUseCase = GetOrdersByUserUseCase(orderRepository),
            insertOrderUseCase = InsertOrderUseCase(orderRepository),
            updateOrderStatusUseCase = UpdateOrderStatusUseCase(orderRepository)
        )
    }

    @Singleton
    @Provides
    fun provideCardUseCases(cardRepository: CardRepository): CardUseCases {
        return CardUseCases(
            getCardByUserIdAndCardIdUseCase = GetCardByUserIdAndCardIdUseCase(cardRepository),
            getCardsByUserIdUseCase = GetCardsByUserIdUseCase(cardRepository),
            insertCardEntityUseCase = InsertCardEntityUseCase(cardRepository),
            cardValidatorUseCase = CardValidatorUseCase()
        )
    }
}