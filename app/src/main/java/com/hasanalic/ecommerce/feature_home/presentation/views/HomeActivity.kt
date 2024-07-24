package com.hasanalic.ecommerce.feature_home.presentation.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.ActivityHomeBinding
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.feature_location.presentation.LocationActivity
import com.hasanalic.ecommerce.feature_notification.presentation.NotificationActivity
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.Constants.CART_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.utils.Constants.CART_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.notification.ReminderItem
import com.hasanalic.ecommerce.utils.notification.cart.CartAlarmScheduler
import com.hasanalic.ecommerce.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null

    //private lateinit var auth: FirebaseAuth
    private var userId: String = Constants.ANOMIM_USER_ID

    private lateinit var cartAlarmScheduler: CartAlarmScheduler

    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var navController = findNavController(R.id.fragmentContainerViewHome)
        binding?.bottomNavigationView?.setupWithNavController(navController)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            userId = it.uid
        }

         */

        cartAlarmScheduler = CartAlarmScheduler(this, userId)

        intent?.let {
            if (it.getStringExtra(this.getString(R.string.notification_from)) == this.getString(R.string.notif_shopping_cart)) {
                navController.navigate(
                    R.id.shoppingCartFragment,
                    null,
                    navOptions {
                        popUpTo(R.id.nav_home) {
                            inclusive = true
                        }
                    }
                )
            }
        }

        binding?.materialCardLocation?.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

        binding?.materialCardNotification?.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        launchCartAlarm()

        observer()
    }

    private fun launchCartAlarm() {
        val reminderItemCart = ReminderItem(CART_ALARM_INTERVAL_TEST.toLong(),CART_ALARM_REQUEST_CODE)
        cartAlarmScheduler.cancel(reminderItemCart)
        cartAlarmScheduler.schedule(reminderItemCart)
    }

    private fun observer() {
        viewModel.cartItemCount.observe(this) {
            setShoppingCartItemSize(it)
        }
    }

    private fun setShoppingCartItemSize(shoppingCartItemSize: Int) {
        val badge = binding?.bottomNavigationView?.getOrCreateBadge(R.id.shoppingCartFragment)
        if (shoppingCartItemSize > 0) {
            badge?.isVisible = true
            badge?.number = shoppingCartItemSize
        } else {
            badge?.isVisible = false
        }
    }

    fun showToolBar() {
        binding?.topAppBarHome?.show()
    }

    fun hideToolBar() {
        binding?.topAppBarHome?.hide()
    }
}