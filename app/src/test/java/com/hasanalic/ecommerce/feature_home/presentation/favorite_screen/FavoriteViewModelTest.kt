package com.hasanalic.ecommerce.feature_home.presentation.favorite_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_home.data.repository.FakeFavoriteRepository
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.DeleteFavoriteUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteIdByUserIdAndProductIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteListByUserIdUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.GetFavoriteProductsUseCase
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.InsertFavoriteAndGetIdUseCase
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
    private lateinit var favoriteUseCases: FavoriteUseCases

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases

    private lateinit var favoriteViewModel: FavoriteViewModel

    @Before
    fun setup() {
        favoriteRepository = FakeFavoriteRepository()
        favoriteUseCases = FavoriteUseCases(
            deleteFavoriteUseCase = DeleteFavoriteUseCase(favoriteRepository),
            getFavoriteIdByUserIdAndProductIdUseCase = GetFavoriteIdByUserIdAndProductIdUseCase(favoriteRepository),
            getFavoriteListByUserIdUseCase = GetFavoriteListByUserIdUseCase(favoriteRepository),
            getFavoriteProductsUseCase = GetFavoriteProductsUseCase(favoriteRepository),
            insertFavoriteAndGetIdUseCase = InsertFavoriteAndGetIdUseCase(favoriteRepository)
        )

        shoppingCartRepository = FakeShoppingCartRepository()
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

        favoriteViewModel = FavoriteViewModel(favoriteUseCases, shoppingCartUseCases)
    }

    @Test
    fun `getUserFavoriteProducts successfuly returns favorite products`() = runBlocking {
        favoriteViewModel.getUserFavoriteProducts("1")
        val state = favoriteViewModel.favoriteState.getOrAwaitValue()

        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromFavorites successfuly removes item`() {
        favoriteViewModel.getUserFavoriteProducts("1")
        favoriteViewModel.removeProductFromFavorites("1","1",0)

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()
        assertThat(state.favoriteProductList).isEmpty()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromFavorites triggers action error when deletion fails`() {
        favoriteViewModel.getUserFavoriteProducts("1")
        favoriteViewModel.removeProductFromFavorites("1","2",0)

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()
        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.actionError).isNotEmpty()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `addProductToCart successfuly adds item to cart`() {
        favoriteViewModel.getUserFavoriteProducts("1")
        favoriteViewModel.addProductToCart("1","1",0)

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()
        assertThat(state.favoriteProductList[0].addedToShoppingCart).isTrue()

        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart successfuly removes item from cart`() {
        favoriteViewModel.getUserFavoriteProducts("1")
        favoriteViewModel.addProductToCart("1","1", 0)
        favoriteViewModel.removeProductFromCart("1","1",0)

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()
        assertThat(state.favoriteProductList[0].addedToShoppingCart).isFalse()

        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart triggers action error when deletion fails`() {
        favoriteViewModel.getUserFavoriteProducts("1")
        favoriteViewModel.addProductToCart("1","1", 0)
        favoriteViewModel.removeProductFromCart("1","2",0)

        val state = favoriteViewModel.favoriteState.getOrAwaitValue()
        assertThat(state.favoriteProductList[0].addedToShoppingCart).isTrue()
        assertThat(state.actionError).isNotEmpty()

        assertThat(state.favoriteProductList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }
}