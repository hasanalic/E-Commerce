package com.hasanalic.ecommerce.feature_checkout.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hasanalic.ecommerce.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
    }
}