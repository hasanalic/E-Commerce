package com.hasanalic.ecommerce.utils.notification

import android.app.PendingIntent

interface AlarmScheduler {
    fun createPendingIntent(reminderItem: ReminderItem): PendingIntent

    fun schedule(reminderItem: ReminderItem)

    fun cancel(reminderItem: ReminderItem)
}