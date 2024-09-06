package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen

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
import com.hasanalic.ecommerce.feature_home.data.repository.FakeShoppingCartRepository
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
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
class ShoppingCartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var shoppingCartUseCases: ShoppingCartUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var shoppingCartViewModel: ShoppingCartViewModel

    @Before
    fun setup() {
        shoppingCartRepository = FakeShoppingCartRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

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
        shoppingCartViewModel = ShoppingCartViewModel(shoppingCartUseCases, sharedPreferencesUseCases)
    }

    @Test
    fun `checkUserAndGetShoppingCartItemList successfully retrieves items`() = runBlocking {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.shoppingCartItemList).isNotEmpty()
        assertThat(state.totalPriceWhole).isNotNull()
        assertThat(state.totalPriceCent).isNotNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
        assertThat(state.actionError).isNull()
    }

    @Test
    fun `getShoppingCartItemList returns data error when user not found`() = runBlocking {
        sharedPreferencesUseCases.logOutUserUseCase()

        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isFalse()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isEqualTo(ANOMIM_USER_ID)

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.totalPriceWhole).isNull()
        assertThat(state.totalPriceCent).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNotEmpty()
    }

    @Test
    fun `removeItemFromShoppingCart successfuly removes item`() = runBlocking {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        shoppingCartViewModel.removeItemFromShoppingCart("1")

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.totalPriceWhole).isNotNull()
        assertThat(state.totalPriceCent).isNotNull()
    }

    @Test
    fun `removeItemFromShoppingCart triggers action error when deletion fails`() = runBlocking {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        shoppingCartViewModel.removeItemFromShoppingCart("2")

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.actionError).isNotEmpty()
        assertThat(state.dataError).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.totalPriceWhole).isNotNull()
        assertThat(state.totalPriceCent).isNotNull()
    }

    @Test
    fun `increaseItemQuantity successfuly increases quantity`() {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        shoppingCartViewModel.increaseItemQuantityInShoppingCart("1",2)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.shoppingCartItemList.first().quantity).isEqualTo(3)
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.totalPriceWhole).isNotNull()
        assertThat(state.totalPriceCent).isNotNull()
    }

    @Test
    fun `decreaseItemQuantity successfuly decreases quantity`() {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        shoppingCartViewModel.decreaseItemQuantityInShoppingCart("1",2)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.shoppingCartItemList.first().quantity).isEqualTo(1)
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.totalPriceWhole).isNotNull()
        assertThat(state.totalPriceCent).isNotNull()
    }

    @Test
    fun `decreaseItemQuantityInShoppingCart removes item when quantity becomes zero`() {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        shoppingCartViewModel.decreaseItemQuantityInShoppingCart("1",2)
        shoppingCartViewModel.decreaseItemQuantityInShoppingCart("1",1)

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.totalPriceWhole).isNotNull()
        assertThat(state.totalPriceCent).isNotNull()
    }

    @Test
    fun `calculateTotalPriceInShoppingCart calculates the correct total price`() {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.totalPriceWhole).isEqualTo(2)
        assertThat(state.totalPriceCent).isEqualTo(2)
        assertThat(state.dataError).isNull()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `completeOrder sets shouldUserMoveToAuthActivity to true when user not logged in`() {
        sharedPreferencesUseCases.logOutUserUseCase()
        shoppingCartViewModel.completeOrder()

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.isUserLoggedIn).isFalse()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isTrue()
        assertThat(state.userId).isEqualTo(ANOMIM_USER_ID)

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.actionError).isNull()
        assertThat(state.dataError).isNull()
        assertThat(state.isLoading).isFalse()
        assertThat(state.totalPriceWhole).isNull()
        assertThat(state.totalPriceCent).isNull()
    }

    @Test
    fun `completeOrder sets shouldUserMoveToAuthActivity to true when shoppingCartItemList empty`() {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        shoppingCartViewModel.removeItemFromShoppingCart("1")
        shoppingCartViewModel.completeOrder()

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.shoppingCartItemList).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.actionError).isNotEmpty()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.canUserMoveToCheckout).isFalse()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.dataError).isNull()
    }

    @Test
    fun `completeOrder sets canUserMoveToCheckout to true when user logged in and shoppingCartItemList is not empty`() {
        shoppingCartViewModel.checkUserAndGetShoppingCartItemList()
        shoppingCartViewModel.completeOrder()

        val state = shoppingCartViewModel.shoppingCartState.getOrAwaitValue()

        assertThat(state.canUserMoveToCheckout).isTrue()
        assertThat(state.shoppingCartItemList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.actionError).isNull()

        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.shouldUserMoveToAuthActivity).isFalse()
        assertThat(state.userId).isNotEqualTo(ANOMIM_USER_ID)

        assertThat(state.dataError).isNull()
    }
}