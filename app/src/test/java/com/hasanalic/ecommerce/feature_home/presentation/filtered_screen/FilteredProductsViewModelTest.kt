package com.hasanalic.ecommerce.feature_home.presentation.filtered_screen

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
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsByCategoryUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetBrandsUseCase
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.GetCategoriesUseCase
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFilteredProductsRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FilteredProductsRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.CheckIfProductInFavoritesUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.DeleteFavoriteUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteListByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.InsertFavoriteAndGetIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases.FilteredProductsUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases.GetProductsByCategoryUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases.GetProductsByFilterUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases.GetProductsByKeywordUseCase
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
class FilteredProductsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var filteredProductsRepository: FilteredProductsRepository
    private lateinit var filterRepository: FilterRepository
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var filteredProductsUseCases: FilteredProductsUseCases
    private lateinit var filterUseCases: FilterUseCases
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var favoriteUseCases: FavoriteUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var filteredProductsViewModel: FilteredProductsViewModel

    @Before
    fun setup() {
        filteredProductsRepository = FakeFilteredProductsRepository()
        filterRepository = FakeFilterRepository()
        shoppingCartRepository = FakeShoppingCartRepository()
        favoriteRepository = FakeFavoriteRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        filteredProductsUseCases = FilteredProductsUseCases(
            getProductsByCategoryUseCase = GetProductsByCategoryUseCase(filteredProductsRepository),
            getProductsByFilterUseCase = GetProductsByFilterUseCase(filteredProductsRepository),
            getProductsByKeywordUseCase = GetProductsByKeywordUseCase(filteredProductsRepository)
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

        filteredProductsViewModel = FilteredProductsViewModel(
            filteredProductsUseCases, shoppingCartUseCases, favoriteUseCases, sharedPreferencesUseCases
        )
    }

    @Test
    fun `checkUserId gets user id from shared preferences and update the state`() {
        filteredProductsViewModel.checkUserId()
        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `getProductsByKeyword gets product list that matched with keyword`() {
        filteredProductsViewModel.checkUserId()

        filteredProductsViewModel.getProductsByKeyword("keyword")

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `getProductsByFilter gets product list that matched with filter`() {
        filteredProductsViewModel.checkUserId()

        val filter = Filter(null, null, null, null, null, null)
        filteredProductsViewModel.getProductsByFilter(filter)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `getProductsByCategory gets product list that matched with filter`() {
        filteredProductsViewModel.checkUserId()


        filteredProductsViewModel.getProductsByCategory("category")

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `checkIfProductAlreadyInCart sets addedToShoppingCart to true when product already in cart`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.checkIfProductAlreadyInCart("1",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToShoppingCart).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `checkIfProductAlreadyInCart insert product to cart and sets addedToShoppingCart to true when product not found in cart`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.checkIfProductAlreadyInCart("2",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToShoppingCart).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `checkIfProductNotInCart sets addedToShoppingCart to false when product not in cart`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.checkIfProductNotInCart("1",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToShoppingCart).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `checkIfProductNotInCart delete product from cart and sets addedToShoppingCart to false when product found in cart`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.checkIfProductNotInCart("1",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToShoppingCart).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `addProductToFavoritesIfUserAuthenticated sets shouldUserMoveToAuthActivity to true when userId not found`() {
        sharedPreferencesUseCases.logOutUserUseCase()
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.addProductToFavoritesIfUserAuthenticated("1",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.userId).isEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isTrue()
    }

    @Test
    fun `addProductToFavoritesIfUserAuthenticated sets addedToFavorites to true when product already in favorites`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.addProductToFavoritesIfUserAuthenticated("1",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToFavorites).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `addProductToFavoritesIfUserAuthenticated sets addedToFavorites to true when product not found in favorites`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.addProductToFavoritesIfUserAuthenticated("2",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToFavorites).isTrue()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `removeProductFromFavoritesIfUserAuthenticated sets shouldUserMoveToAuthActivity to true when userId not found`() {
        sharedPreferencesUseCases.logOutUserUseCase()
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.removeProductFromFavoritesIfUserAuthenticated("1",0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.userId).isEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isTrue()
    }

    @Test
    fun `removeProductFromFavoritesIfUserAuthenticated sets addedToFavorites to false when product found in favorites`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.removeProductFromFavoritesIfUserAuthenticated("1", 0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToFavorites).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }

    @Test
    fun `removeProductFromFavoritesIfUserAuthenticated sets addedToFavorites to false when product not found in favorites`() {
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.checkUserId()
        filteredProductsViewModel.getProductsByKeyword("keyword")

        filteredProductsViewModel.removeProductFromFavoritesIfUserAuthenticated("2", 0)

        val state = filteredProductsViewModel.filteredProducsState.getOrAwaitValue()

        assertThat(state.productList.first().addedToFavorites).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)
        assertThat(state.isLoading).isFalse()
        assertThat(state.productList).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
    }
}