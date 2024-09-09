package com.hasanalic.ecommerce.notification.adds

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.notification.Notifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class AddsNotifier(
    private val notificationManager: NotificationManager,
    private val context: Context
) : Notifier(notificationManager) {

    override val notificationChannelId: String = "adds_channel_id"
    override val notificationChannelName: String = "Adds Notifications"
    override val notificationId: Int = 200

    override fun buildNotification(): Notification {
        val randomIndex = Random.nextInt(14)

        val notificationTitle = getNotificationTitle(randomIndex)
        val notificationMessage = getNotificationMessage(randomIndex)

        /*
        val shoppingCartItemsDao = Room.databaseBuilder(context, MyDatabase::class.java, "MyDatabase").build().shoppingCartItemsDao()
        val notificationDao = Room.databaseBuilder(context, MyDatabase::class.java, "MyDatabase").build().notificationDao()
        val serviceRepository = ServiceRepositoryImp(shoppingCartItemsDao, notificationDao)

         */

        CoroutineScope(Dispatchers.IO).launch {
            /*
            val response = serviceRepository.insertNotification(
                NotificationEntity(
                    userId = "",
                    notificationTitle = notificationTitle,
                    notificationContent = notificationMessage,
                    notificationTime = System.currentTimeMillis()
                )
            )

            if (response is Resource.Error) {
            }

             */
        }

        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(getNotificationTitle(randomIndex))
            .setContentText(getNotificationMessage(randomIndex))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()

    }

    override fun getNotificationTitle(random: Int): String {
        val notificationTitles = listOf(
            "Winter Sale Has Started! ❄\uFE0F",
            "New Year's Special Campaign! ☃\uFE0F",
            "Special Discount Opportunity! \uD83D\uDCB0",
            "Weekend Deals Have Started! ⭐",
            "New Product Arrived! \uD83D\uDC7B",
            "Don't Miss the Offers! \uD83C\uDFAF",
            "Don't Miss Weekend Discounts! \uD83D\uDECD\uFE0F",
            "Special Discounts for Today Only! \uD83C\uDF81",
            "New Collection In Stock! \uD83D\uDD25",
            "Only One Opportunity, Don't Miss It! \uD83E\uDD16",
            "The Big Summer Sale Has Started! \uD83D\uDC7D",
            "En Yeni Ürünler Burada! \uD83C\uDF1E",
            "The Newest Products Are Here! \uD83D\uDD54",
            "Feel the Wind! \uD83D\uDC40"
        )
        return notificationTitles[random]
    }

    override fun getNotificationMessage(random: Int): String {
        val notificationMessages = listOf(
            "Start shopping with special discounts for cold winter days!",
            "New Year special campaigns and surprises await you!",
            "Today, special discount opportunities await you just for you. Start shopping now!",
            "Enjoy shopping this weekend, discounts are unmissable!",
            "Our new product is in stock. Click to discover now!",
            "Unmissable offers await you!",
            "Don't miss our discounts valid throughout the weekend. Enjoy shopping!",
            "Today's special discounts and campaigns await you. Hurry up, don't miss the opportunities!",
            "Renew your style with our new collection. Take a look now!",
            "Don't miss the opportunities, your favorite products are now waiting for you at very affordable prices!",
            "Take action now! It's the right time to do your summer shopping.",
            "Discover our newest products. Click for a shopping experience full of surprises!",
            "Last day, don't miss! Start shopping now for discounted products and campaigns!",
            "Rediscover your style with renewed collections!"
        )
        return notificationMessages[random]
    }
}