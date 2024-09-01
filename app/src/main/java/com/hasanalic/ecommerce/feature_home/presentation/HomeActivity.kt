package com.hasanalic.ecommerce.feature_home.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.CART_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.CART_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.databinding.ActivityHomeBinding
import com.hasanalic.ecommerce.notification.ReminderItem
import com.hasanalic.ecommerce.notification.cart.CartAlarmScheduler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null

    private var userId: String = ANOMIM_USER_ID

    private lateinit var cartAlarmScheduler: CartAlarmScheduler

    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var navController = findNavController(R.id.fragmentContainerViewHome)
        binding?.bottomNavigationView?.setupWithNavController(navController)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

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
}