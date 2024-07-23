package com.hasanalic.ecommerce.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hasanalic.ecommerce.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
    }
}