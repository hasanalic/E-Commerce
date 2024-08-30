package com.hasanalic.ecommerce.feature_auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.ADDS_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.ADDS_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.databinding.ActivityAuthBinding
import com.hasanalic.ecommerce.feature_auth.presentation.login.views.LoginFragment
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.notification.ReminderItem
import com.hasanalic.ecommerce.notification.adds.AddsAlarmSchedular
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.feature_home.presentation.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel

    private val addsAlarmScheduler by lazy {
        AddsAlarmSchedular(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        handleDeepLink(intent)

        handleAlarms()

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.authState.observe(this) { authState ->
            if (authState.isUserAlreadyLoggedIn) {
                navigateToHomeActivity()
            }

            if (authState.isLoading) {
                binding.progressBarAuth.show()
            } else {
                binding.progressBarAuth.hide()
            }

            if (authState.isDatabaseInitialized) {
                showFragment(LoginFragment())
            }

            authState.dataError?.let {
                Toast.makeText(this@AuthActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAlarms() {
        // launchAddsAlarm()
    }

    private fun launchAddsAlarm() {
        val reminderItem = ReminderItem(ADDS_ALARM_INTERVAL_TEST.toLong(),ADDS_ALARM_REQUEST_CODE)
        addsAlarmScheduler.cancel(reminderItem)
        addsAlarmScheduler.schedule(reminderItem)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let {uri ->
            if (uri.host == "www.eticaretuygulamasi.com" && uri.pathSegments.contains("urun_detay")) {
                val productId = uri.getQueryParameter("id")
                val productDetailIntent = Intent(this, ProductDetailActivity::class.java)
                productDetailIntent.putExtra(getString(R.string.product_id), productId)
                startActivity(productDetailIntent)
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.fragmentContainerViewMainActivity, fragment)
            commit()
        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}