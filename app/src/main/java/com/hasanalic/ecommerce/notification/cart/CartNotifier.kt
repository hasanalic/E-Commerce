package com.hasanalic.ecommerce.notification.cart

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.feature_home.presentation.HomeActivity
import com.hasanalic.ecommerce.notification.Notifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CartNotifier(
    private val userId: String,
    private val notificationManager: NotificationManager,
    private val context: Context
) : Notifier(notificationManager) {

    override val notificationChannelId: String = "cart_channel_id"
    override val notificationChannelName: String = "Cart Notifications"
    override val notificationId: Int = 300

    override fun buildNotification(): Notification? {
        /*
        val shoppingCartItemsDao = Room.databaseBuilder(context, MyDatabase::class.java, "MyDatabase").build().shoppingCartItemsDao()
        val notificationDao = Room.databaseBuilder(context, MyDatabase::class.java, "MyDatabase").build().notificationDao()
        val serviceRepository = ServiceRepositoryImp(shoppingCartItemsDao, notificationDao)

         */

        var shoppingItemCount = 0

        var notification: Notification? = null

        val intent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(context.getString(R.string.notification_from), context.getString(R.string.notif_shopping_cart))
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val job  = CoroutineScope(Dispatchers.IO).launch {
            /*
            shoppingItemCount = serviceRepository.getShoppingCartCount(userId)

            if (shoppingItemCount > 0) {
                notification = NotificationCompat.Builder(context, notificationChannelId)
                    .setContentTitle("Merhaba!")
                    .setContentText("Sepetinde hala bekleyen $shoppingItemCount ürün var. Hemen alışverişi tamamla!")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            }

             */
        }

        runBlocking {
            job.join()
        }

        return notification
    }

    override fun getNotificationTitle(random: Int): String {
        return "Hello!"
    }

    override fun getNotificationMessage(random: Int): String {
        return ""
    }
}