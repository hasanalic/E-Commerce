package com.hasanalic.ecommerce.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.ActivityCustomerServiceBinding

class CustomerService : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBarCustomer.setNavigationOnClickListener {
            finish()
        }

        binding.buttonReview.setOnClickListener {
            if (binding.review.text.toString() == "") {
                Toast.makeText(this, "Lütfen bir mesaj giriniz", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }
}