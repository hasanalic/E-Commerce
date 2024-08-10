package com.hasanalic.ecommerce.feature_product_detail.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.feature_product_detail.presentation.views.ProductDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intentFromHome = intent
        val productId = intentFromHome.getStringExtra(getString(R.string.product_id))

        moveToProductDetailFragment(productId)
    }

    private fun moveToProductDetailFragment(productId: String?) {
        val bundle = Bundle()
        bundle.putString(getString(R.string.product_id),productId)
        val productDetailFragment = ProductDetailFragment()
        productDetailFragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.fragmentContainerViewProductDetailActivity, productDetailFragment)
            commit()
        }
    }
}