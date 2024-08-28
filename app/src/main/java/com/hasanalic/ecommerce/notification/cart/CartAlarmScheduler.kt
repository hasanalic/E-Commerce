package com.hasanalic.ecommerce.notification.cart

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.notification.AlarmScheduler
import com.hasanalic.ecommerce.notification.ReminderItem

class CartAlarmScheduler(
    private val context: Context,
    private val userId: String
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun createPendingIntent(reminderItem: ReminderItem): PendingIntent {
        val intent = Intent(context, CartAlarmReceiver::class.java)
        intent.putExtra(context.getString(R.string.user_id),userId)

        return PendingIntent.getBroadcast(
            context,
            reminderItem.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun schedule(reminderItem: ReminderItem) {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            reminderItem.time,
            createPendingIntent(reminderItem)
        )
    }

    override fun cancel(reminderItem: ReminderItem) {
        alarmManager.cancel(
            createPendingIntent(reminderItem)
        )
    }
}