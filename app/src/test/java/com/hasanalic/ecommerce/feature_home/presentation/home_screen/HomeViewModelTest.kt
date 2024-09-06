package com.hasanalic.ecommerce.feature_home.presentation.home_screen

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
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_filter.data.FakeFilterRepository
import com.hasanalic.ecommerce.feature_filter.domain.repository.FilterRepository
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.FilterUseCases
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeHomeRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.DeleteFavoriteUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteListByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.InsertFavoriteAndGetIdUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsByCategoryUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetCategoriesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.CheckIfProductInFavoritesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.GetProductEntityIdByBarcodeUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.GetProductsByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.HomeUseCases
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var homeRepository: HomeRepository
    private lateinit var filterRepository: FilterRepository
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var homeUseCases: HomeUseCases
    private lateinit var filterUseCases: FilterUseCases
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var favoriteUseCases: FavoriteUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        homeRepository = FakeHomeRepository()
        filterRepository = FakeFilterRepository()
        shoppingCartRepository = FakeShoppingCartRepository()
        favoriteRepository = FakeFavoriteRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        homeUseCases = HomeUseCases(
            getProductsByUserIdUseCase = GetProductsByUserIdUseCase(homeRepository),
            getProductEntityIdByBarcodeUseCase = GetProductEntityIdByBarcodeUseCase(homeRepository)
        )

        filterUseCases = FilterUseCases(
            getBrandsUseCase = GetBrandsUseCase(filterRepository),
            getBrandsByCategoryUseCase = GetBrandsByCategoryUseCase(filterRepository),
            getCategoriesUseCase = GetCategoriesUseCase(filterRepository)
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

        favoriteUseCases = FavoriteUseCases(
            deleteFavoriteUseCase = DeleteFavoriteUseCase(favoriteRepository),
            getFavoriteListByUserIdUseCase = GetFavoriteListByUserIdUseCase(favoriteRepository),
            getFavoriteProductsUseCase = GetFavoriteProductsUseCase(favoriteRepository),
            insertFavoriteAndGetIdUseCase = InsertFavoriteAndGetIdUseCase(favoriteRepository),
            checkIfProductInFavoritesUseCase = CheckIfProductInFavoritesUseCase(favoriteRepository)
        )

        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        sharedPreferencesUseCases.saveUserIdUseCase("1")

        homeViewModel = HomeViewModel(homeUseCases, filterUseCases, shoppingCartUseCases, favoriteUseCases, sharedPreferencesUseCases)
    }

    @Test
    fun `getCategories successfuly returns category list`() {
        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.categoryList).isNotEmpty()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `getProducts successfuly returns product list`() {
        homeViewModel.getProducts()
        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart successfuly removes product from cart and update the product list`() {
        homeViewModel.getProducts()
        homeViewModel.addProductToCart("1",0)
        homeViewModel.removeProductFromCart("1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToShoppingCart).isFalse()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart triggers action error when deletion fails`() {
        homeViewModel.getProducts()
        homeViewModel.addProductToCart("1",0)
        homeViewModel.removeProductFromCart("2",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToShoppingCart).isTrue()
        assertThat(state.actionError).isNotEmpty()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `addProductToCart successfuly adds product to cart and update the product list`() {
        homeViewModel.getProducts()
        homeViewModel.addProductToCart("1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToShoppingCart).isTrue()
        assertThat(state.actionError).isNull()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `removeProductFromFavoritesIfUserAuthenticated successfuly removes product from favorites and update the product list`() {
        homeViewModel.getProducts()
        homeViewModel.addProductToFavoritesIfUserAuthenticated("1",0)
        homeViewModel.removeProductFromFavoritesIfUserAuthenticated("1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToFavorites).isFalse()
        assertThat(state.actionError).isNull()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `removeProductFromFavoritesIfUserAuthenticated triggers action error when deletion fails`() {
        homeViewModel.getProducts()
        homeViewModel.addProductToFavoritesIfUserAuthenticated("1",0)
        homeViewModel.removeProductFromFavoritesIfUserAuthenticated("2",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToFavorites).isTrue()
        assertThat(state.actionError).isNotEmpty()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `addProductToFavoritesIfUserAuthenticated successfuly adds product to favorites and update the product list`() {
        homeViewModel.getProducts()
        homeViewModel.addProductToFavoritesIfUserAuthenticated("1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToFavorites).isTrue()
        assertThat(state.actionError).isNull()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.scannedProductId).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `getProductIdByBarcode successfuly fetches productId and update the state`() {
        homeViewModel.getProductIdByBarcode("123412341234")
        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.scannedProductId).isNotEmpty()
        assertThat(state.actionError).isNull()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `setScannedProductIdToNull sets scannedProductId to null and update the state`() {
        homeViewModel.getProductIdByBarcode("123412341234")
        homeViewModel.setScannedProductIdToNull()
        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.scannedProductId).isNull()
        assertThat(state.actionError).isNull()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }
}