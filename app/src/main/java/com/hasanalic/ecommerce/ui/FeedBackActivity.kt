package com.hasanalic.ecommerce.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hasanalic.ecommerce.databinding.ActivityFeedBackBinding

class FeedBackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBarFeedback.setNavigationOnClickListener {
            finish()
        }

        binding.buttonReview.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            if (name == "" || email == "") {
                Toast.makeText(this,"Lütfen, ilgili alanları doldurunuz",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Değerlendirmeniz gönderilmiştir",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}