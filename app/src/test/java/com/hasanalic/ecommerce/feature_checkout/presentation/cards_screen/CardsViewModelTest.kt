package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen

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
import com.hasanalic.ecommerce.feature_checkout.data.repository.FakeCardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.repository.CardRepository
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardValidatorUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardByUserIdAndCardIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.GetCardsByUserIdUseCase
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.InsertCardEntityUseCase
import com.hasanalic.ecommerce.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CardsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var cardRepository: CardRepository
    private lateinit var sharedPreferencesDataSource: SharedPreferencesDataSource

    private lateinit var cardUseCases: CardUseCases
    private lateinit var sharedPreferencesUseCases: SharedPreferencesUseCases

    private lateinit var cardsViewModel: CardsViewModel

    @Before
    fun setup() {
        cardRepository = FakeCardRepository()
        sharedPreferencesDataSource = FakeSharedPreferencesDataSourceImp()

        cardUseCases = CardUseCases(
            getCardByUserIdAndCardIdUseCase = GetCardByUserIdAndCardIdUseCase(cardRepository),
            getCardsByUserIdUseCase = GetCardsByUserIdUseCase(cardRepository),
            insertCardEntityUseCase = InsertCardEntityUseCase(cardRepository),
            cardValidatorUseCase = CardValidatorUseCase()
        )
        sharedPreferencesUseCases = SharedPreferencesUseCases(
            getUserIdUseCase = GetUserIdUseCase(sharedPreferencesDataSource),
            isDatabaseInitializedUseCase = IsDatabaseInitializedUseCase(sharedPreferencesDataSource),
            saveUserIdUseCase = SaveUserIdUseCase(sharedPreferencesDataSource),
            setDatabaseInitializedUseCase = SetDatabaseInitializedUseCase(sharedPreferencesDataSource),
            logOutUserUseCase = LogOutUserUseCase(sharedPreferencesDataSource)
        )

        sharedPreferencesUseCases.saveUserIdUseCase("1")
        cardsViewModel = CardsViewModel(cardUseCases, sharedPreferencesUseCases)
    }

    @Test
    fun `getUserCards should returns card list when found`() {
        cardsViewModel.getUserCards()
        val state = cardsViewModel.cardsState.getOrAwaitValue()

        assertThat(state.cardList).isNotEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNull()
    }

    @Test
    fun `getUserCards should triggers data error when not found`() {
        sharedPreferencesUseCases.logOutUserUseCase()
        cardsViewModel.getUserCards()
        val state = cardsViewModel.cardsState.getOrAwaitValue()

        assertThat(state.cardList).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.dataError).isNotEmpty()
    }
}