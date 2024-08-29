package com.hasanalic.ecommerce.feature_auth.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.ADDS_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.ADDS_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.feature_auth.presentation.login.views.LoginFragment
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.utils.CustomSharedPreferences
import com.hasanalic.ecommerce.core.domain.utils.DatabaseInitializerUtil
import com.hasanalic.ecommerce.notification.ReminderItem
import com.hasanalic.ecommerce.notification.adds.AddsAlarmSchedular
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var databaseInitializerUtil: DatabaseInitializerUtil

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
        lifecycleScope.launch {
            try {
                databaseInitializerUtil.insertProductEntities()
                databaseInitializerUtil.insertReviews()
            } catch (e: Exception) {
                Toast.makeText(this@AuthActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}