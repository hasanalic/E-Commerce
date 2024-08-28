package com.hasanalic.ecommerce.notification.cart

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hasanalic.ecommerce.R

class CartAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val userId = intent.getStringExtra(context.getString(R.string.user_id))?:""

            val cartNotifier = CartNotifier(userId, notificationManager, context)
            cartNotifier.showNotification()
        }
    }
}