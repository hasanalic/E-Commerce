package com.hasanalic.ecommerce.feature_home.presentation.home_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeHomeRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.DeleteFavoriteUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteIdByUserIdAndProductIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteListByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.InsertFavoriteAndGetIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.GetBrandsByCategoryUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.GetBrandsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.GetCategoriesUseCase
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
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var homeUseCases: HomeUseCases
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var favoriteUseCases: FavoriteUseCases

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        homeRepository = FakeHomeRepository()
        shoppingCartRepository = FakeShoppingCartRepository()
        favoriteRepository = FakeFavoriteRepository()

        homeUseCases = HomeUseCases(
            getProductsByUserIdUseCase = GetProductsByUserIdUseCase(homeRepository),
            getCategoriesUseCase = GetCategoriesUseCase(homeRepository),
            getBrandsUseCase = GetBrandsUseCase(homeRepository),
            getBrandsByCategoryUseCase = GetBrandsByCategoryUseCase(homeRepository),
            getProductEntityIdByBarcodeUseCase = GetProductEntityIdByBarcodeUseCase(homeRepository)
        )

        shoppingCartUseCases = ShoppingCartUseCases(
            checkShoppingCartEntityByProductIdUseCase = CheckShoppingCartEntityByProductIdUseCase(shoppingCartRepository),
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
            getFavoriteIdByUserIdAndProductIdUseCase = GetFavoriteIdByUserIdAndProductIdUseCase(favoriteRepository),
            getFavoriteListByUserIdUseCase = GetFavoriteListByUserIdUseCase(favoriteRepository),
            getFavoriteProductsUseCase = GetFavoriteProductsUseCase(favoriteRepository),
            insertFavoriteAndGetIdUseCase = InsertFavoriteAndGetIdUseCase(favoriteRepository)
        )

        homeViewModel = HomeViewModel(homeUseCases, shoppingCartUseCases, favoriteUseCases)
    }

    @Test
    fun `getCategories successfuly returns category list`() {
        homeViewModel.getCategories()
        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.categoryList).isNotEmpty()

        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `getProducts successfuly returns product list`() {
        homeViewModel.getProducts("1")
        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()

        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart successfuly removes product from cart and update the product list`() {
        homeViewModel.getProducts("1")
        homeViewModel.addProductToCart("1","1",0)
        homeViewModel.removeProductFromCart("1","1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToShoppingCart).isFalse()

        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart triggers action error when deletion fails`() {
        homeViewModel.getProducts("1")
        homeViewModel.addProductToCart("1","1",0)
        homeViewModel.removeProductFromCart("1","2",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToShoppingCart).isTrue()
        assertThat(state.actionError).isNotEmpty()

        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `addProductToCart successfuly adds product to cart and update the product list`() {
        homeViewModel.getProducts("1")
        homeViewModel.addProductToCart("1","1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToShoppingCart).isTrue()
        assertThat(state.actionError).isNull()

        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `removeProductFromFavorites successfuly removes product from favorites and update the product list`() {
        homeViewModel.getProducts("1")
        homeViewModel.addProductToFavorites("1","1",0)
        homeViewModel.removeProductFromFavorites("1","1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToFavorites).isFalse()
        assertThat(state.actionError).isNull()

        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `removeProductFromFavorites triggers action error when deletion fails`() {
        homeViewModel.getProducts("1")
        homeViewModel.addProductToFavorites("1","1",0)
        homeViewModel.removeProductFromFavorites("1","2",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToFavorites).isTrue()
        assertThat(state.actionError).isNotEmpty()

        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `addProductToFavorites successfuly adds product to favorites and update the product list`() {
        homeViewModel.getProducts("1")
        homeViewModel.addProductToFavorites("1","1",0)

        val state = homeViewModel.homeState.getOrAwaitValue()

        assertThat(state.productList).isNotEmpty()
        assertThat(state.productList[0].addedToFavorites).isTrue()
        assertThat(state.actionError).isNull()

        assertThat(state.isLoading).isFalse()
        assertThat(state.categoryList).isNotEmpty()
        assertThat(state.dataError).isNull()
    }

}