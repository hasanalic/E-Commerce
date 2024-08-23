package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen

import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity

data class CardsState (
    val isLoading: Boolean = false,
    val cardList: List<CardEntity> = listOf(),
    val dataError: String? = null
)