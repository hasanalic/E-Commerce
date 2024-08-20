package com.hasanalic.ecommerce.feature_orders.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hasanalic.ecommerce.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
    }
}