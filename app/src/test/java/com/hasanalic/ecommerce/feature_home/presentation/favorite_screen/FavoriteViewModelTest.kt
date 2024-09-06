package com.hasanalic.ecommerce.feature_home.presentation.favorite_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.core.data.FakeSharedPreferencesDataSourceImp
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.GetUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.IsDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.LogOutUserUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SaveUserIdUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SetDatabaseInitializedUseCase
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.CheckIfProductInFavoritesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.DeleteFavoriteUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteListByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.InsertFavoriteAndGetIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.CheckIfProductInCartUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.DeleteShoppingCartItemEntitiesByProductIdListUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.DeleteShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.GetProductsInShoppingCartUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.GetShoppingCartItemCountUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.InsertAllShoppingCartItemEntitiesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.InsertShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.UpdateShoppingCartItemEntityUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var favoriteUseCases: FavoriteUseCases
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var favoriteViewModel: FavoriteViewModel

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        shoppingCartRepository = FakeShoppingCartRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        favoriteUseCases = FavoriteUseCases(
            deleteFavoriteUseCase = DeleteFavoriteUseCase(favoriteRepository),
            getFavoriteListByUserIdUseCase = GetFavoriteListByUserIdUseCase(favoriteRepository),
            getFavoriteProductsUseCase = GetFavoriteProductsUseCase(favoriteRepository),
            insertFavoriteAndGetIdUseCase = InsertFavoriteAndGetIdUseCase(favoriteRepository),
            checkIfProductInFavoritesUseCase = CheckIfProductInFavoritesUseCase(favoriteRepository)
        )
        shoppingCartUseCases = ShoppingCartUseCases(
            checkIfProductInCartUseCase = CheckIfProductInCartUseCase(shoppingCartRepository),
            deleteShoppingCartItemEntitiesByProductIdListUseCase = DeleteShoppingCartItemEntitiesByProductIdListUseCase(shoppingCartRepository),
            deleteShoppingCartItemEntityUseCase = DeleteShoppingCartItemEntityUseCase(shoppingCartRepository),
            getProductsInShoppingCartUseCase = GetProductsInShoppingCartUseCase(shoppingCartRepository),
            getShoppingCartItemCountUseCase = GetShoppingCartItemCountUseCase(shoppingCartRepository),
            insertAllShoppingCartItemEntitiesUseCase = InsertAllShoppingCartItemEntitiesUseCase(shoppingCartRepository),
            insertShoppingCartItemEntityUseCase = InsertShoppingCartItemEntityUseCase(shoppingCartRepository),
            updateShoppingCartItemEntityUseCase = UpdateShoppingCartItemEntityUseCase(shoppingCartRepository)
        )
        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        sharedPreferencesUseCases.saveUserIdUseCase("1")
        favoriteViewModel = FavoriteViewModel(favoriteUseCases, shoppingCartUseCases, sharedPreferencesUseCases)
    }

    @Test
    fun `getUserFavoriteProducts successfuly returns favorite products when user logged in`() = runBlocking {
        favoriteViewModel.getUserFavoriteProductsIfUserLoggedIn()

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()

        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `getUserFavoriteProducts successfuly returns favorite products when user logged out`() {
        sharedPreferencesUseCases.logOutUserUseCase()
        favoriteViewModel.getUserFavoriteProductsIfUserLoggedIn()

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()

        assertThat(state.favoriteProductList).isEmpty()
        assertThat(state.isUserLoggedIn).isFalse()
        assertThat(state.userId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromFavorites successfuly removes item`() {
        favoriteViewModel.getUserFavoriteProductsIfUserLoggedIn()

        favoriteViewModel.removeProductFromFavorites("1")

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()

        assertThat(state.favoriteProductList).isEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromFavorites triggers action error when deletion fails`() {
        favoriteViewModel.getUserFavoriteProductsIfUserLoggedIn()

        favoriteViewModel.removeProductFromFavorites("2")

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()

        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.actionError).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `addProductToCart successfuly adds item to cart`() {
        favoriteViewModel.getUserFavoriteProductsIfUserLoggedIn()

        favoriteViewModel.addProductToCart("1")

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()
        assertThat(state.favoriteProductList[0].addedToShoppingCart).isTrue()

        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart successfuly removes item from cart`() {
        favoriteViewModel.getUserFavoriteProductsIfUserLoggedIn()

        favoriteViewModel.addProductToCart("1")
        favoriteViewModel.removeProductFromCart("1")

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()

        assertThat(state.favoriteProductList[0].addedToShoppingCart).isFalse()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEmpty()
        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart triggers action error when deletion fails`() {
        favoriteViewModel.getUserFavoriteProductsIfUserLoggedIn()

        favoriteViewModel.addProductToCart("1")
        favoriteViewModel.removeProductFromCart("2")

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()

        assertThat(state.favoriteProductList[0].addedToShoppingCart).isTrue()
        assertThat(state.actionError).isNotEmpty()
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.userId).isNotEmpty()
        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }
}