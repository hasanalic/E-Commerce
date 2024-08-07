package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.hasanalic.ecommerce.MainCoroutineRule
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
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
class ShoppingCartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var shoppingCartViewModel: ShoppingCartViewModel

    @Before
    fun setup() {
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
        shoppingCartViewModel = ShoppingCartViewModel(shoppingCartUseCases)
    }

    @Test
    fun `getShoppingCartItemList successfully retrieves items`() = runBlocking {
        shoppingCartViewModel.getShoppingCartItemList("1")
        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.shoppingCartItemList).isNotEmpty()
        assertThat(state.totalPriceWhole).isNotNull()
        assertThat(state.totalPriceCent).isNotNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `getShoppingCartItemList returns data error when user not found`() = runBlocking {
        shoppingCartViewModel.getShoppingCartItemList("2")
        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.totalPriceWhole).isNull()
        assertThat(state.totalPriceCent).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNotEmpty()
    }

    @Test
    fun `removeItemFromShoppingCart successfuly removes item`() = runBlocking {
        shoppingCartViewModel.getShoppingCartItemList("1")
        shoppingCartViewModel.removeItemFromShoppingCart("1","1",0)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `removeItemFromShoppingCart returns action error when deletion fails`() = runBlocking {
        shoppingCartViewModel.getShoppingCartItemList("1")
        shoppingCartViewModel.removeItemFromShoppingCart("1","2",0)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.actionError).isNotEmpty()
    }

    @Test
    fun `increaseItemQuantity successfuly increases quantity`() {
        shoppingCartViewModel.getShoppingCartItemList("1")
        shoppingCartViewModel.increaseItemQuantityInShoppingCart("1","1",2,0)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.shoppingCartItemList.first().quantity).isEqualTo(3)
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `decreaseItemQuantity successfuly decreases quantity`() {
        shoppingCartViewModel.getShoppingCartItemList("1")
        shoppingCartViewModel.decreaseItemQuantityInShoppingCart("1","1",2, 0)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.shoppingCartItemList.first().quantity).isEqualTo(1)
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `decreaseItemQuantityInShoppingCart removes item when quantity becomes zero`() {
        shoppingCartViewModel.getShoppingCartItemList("1")
        shoppingCartViewModel.decreaseItemQuantityInShoppingCart("1","1",2, 0)
        shoppingCartViewModel.decreaseItemQuantityInShoppingCart("1","1",1, 0)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `calculateTotalPriceInShoppingCart calculates the correct total price`() {
        shoppingCartViewModel.getShoppingCartItemList("1")
        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.totalPriceWhole).isEqualTo(2)
        assertThat(state.totalPriceCent).isEqualTo(2)
    }
}