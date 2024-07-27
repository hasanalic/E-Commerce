package com.hasanalic.ecommerce.feature_auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.feature_auth.presentation.login.LoginFragment
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.utils.Constants.ADDS_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.utils.Constants.ADDS_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.utils.CustomSharedPreferences
import com.hasanalic.ecommerce.utils.DatabaseInitializer
import com.hasanalic.ecommerce.utils.notification.ReminderItem
import com.hasanalic.ecommerce.utils.notification.adds.AddsAlarmSchedular
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    private val addsAlarmScheduler by lazy {
        AddsAlarmSchedular(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (CustomSharedPreferences(this).getDatabaseInitialization() as Boolean) {
            startDatabaseInitialization()
            CustomSharedPreferences(this).setDatabaseInitialization(false)
        }

        showFragment(LoginFragment())

        handleDeepLink(intent)

        //launchAddsAlarm()
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

    private fun startDatabaseInitialization() {
        CoroutineScope(Dispatchers.Default).launch {
            databaseInitializer.initializeProducts(this@AuthActivity)
            databaseInitializer.initializeReviews()
        }
    }
}