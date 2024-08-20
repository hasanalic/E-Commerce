package com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen

import androidx.lifecycle.ViewModel
import com.hasanalic.ecommerce.feature_orders.domain.use_cases.OrderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases
) : ViewModel() {


}