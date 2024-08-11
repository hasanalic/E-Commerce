package com.hasanalic.ecommerce.feature_product_detail.presentation

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
import com.hasanalic.ecommerce.feature_product_detail.data.repository.FakeProductDetailRepository
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository
import com.hasanalic.ecommerce.feature_product_detail.domain.use_cases.GetProductDetailByUserIdAndProductIdUseCase
import com.hasanalic.ecommerce.feature_product_detail.domain.use_cases.GetReviewsByProductIdUseCase
import com.hasanalic.ecommerce.feature_product_detail.domain.use_cases.ProductDetailUseCases
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var productDetailRepository: ProductDetailRepository
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var productDetailUseCases: ProductDetailUseCases
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var favoriteUseCases: FavoriteUseCases

    private lateinit var productDetailViewModel: ProductDetailViewModel

    @Before
    fun setup() {
        productDetailRepository = FakeProductDetailRepository()
        shoppingCartRepository = FakeShoppingCartRepository()
        favoriteRepository = FakeFavoriteRepository()

        productDetailUseCases = ProductDetailUseCases(
            getProductDetailByUserIdAndProductIdUseCase = GetProductDetailByUserIdAndProductIdUseCase(productDetailRepository),
            getReviewsByProductIdUseCase = GetReviewsByProductIdUseCase(productDetailRepository)
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

        productDetailViewModel = ProductDetailViewModel(productDetailUseCases, shoppingCartUseCases, favoriteUseCases)
    }

    @Test
    fun `getProductDetail successfuly returns product detail`() {
        productDetailViewModel.getProductDetailAndReviews("1","1")
        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNotNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `getProductDetail triggers data error when product id not found`() {
        productDetailViewModel.getProductDetailAndReviews("1","invalid")
        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNotEmpty()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `getProductReviews successfuly returns review list`() {
        productDetailViewModel.getProductReviews("1")
        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.reviewList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `addProductToCart successfuly adds product to cart and update the product detail`() {
        productDetailViewModel.getProductDetailAndReviews("1","1")
        productDetailViewModel.addProductToCart("1","1")

        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNotNull()
        assertThat(state.productDetail?.addedToShoppingCart).isTrue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart successfuly removes product from cart and update the product detail`() {
        productDetailViewModel.getProductDetailAndReviews("1","1")
        productDetailViewModel.addProductToCart("1","1")
        productDetailViewModel.removeProductFromCart("1","1")

        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNotNull()
        assertThat(state.productDetail?.addedToShoppingCart).isFalse()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromCart triggers action error when deletion fails`() {
        productDetailViewModel.getProductDetailAndReviews("1","1")
        productDetailViewModel.addProductToCart("1","1")
        productDetailViewModel.removeProductFromCart("1","invalid")

        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNotNull()
        assertThat(state.productDetail?.addedToShoppingCart).isTrue()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNotEmpty()
    }

    @Test
    fun `addProductToFavorites successfuly adds product to favorites and update the product detail`() {
        productDetailViewModel.getProductDetailAndReviews("1","1")
        productDetailViewModel.addProductToFavorites("1","1")

        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNotNull()
        assertThat(state.productDetail?.addedToFavorites).isTrue()
        assertThat(state.productDetail?.addedToShoppingCart).isFalse()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromFavorites successfuly removes product from favorites and update the product detail`() {
        productDetailViewModel.getProductDetailAndReviews("1","1")
        productDetailViewModel.addProductToFavorites("1","1")
        productDetailViewModel.removeProductFromFavorites("1","1")

        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNotNull()
        assertThat(state.productDetail?.addedToFavorites).isFalse()
        assertThat(state.productDetail?.addedToShoppingCart).isFalse()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeProductFromFavorites triggers action error when deletion fails`() {
        productDetailViewModel.getProductDetailAndReviews("1","1")
        productDetailViewModel.addProductToFavorites("1","1")
        productDetailViewModel.removeProductFromFavorites("1","invalid")

        val state = productDetailViewModel.productDetailState.getOrAwaitValue()

        assertThat(state.productDetail).isNotNull()
        assertThat(state.productDetail?.addedToFavorites).isTrue()
        assertThat(state.productDetail?.addedToShoppingCart).isFalse()

        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNotEmpty()
    }
}