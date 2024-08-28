package com.hasanalic.ecommerce.notification.adds

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val addsNotifier = AddsNotifier(notificationManager, it)
            addsNotifier.showNotification()
        }
    }
}