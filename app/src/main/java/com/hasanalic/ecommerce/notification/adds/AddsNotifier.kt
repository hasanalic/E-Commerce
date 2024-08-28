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
    override val notificationChannelName: String = "Reklam Bildirimleri"
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
            "Kış İndirimi Başladı! ❄\uFE0F",
            "Yılbaşı Özel Kampanyası! ☃\uFE0F",
            "Özel İndirim Fırsatı! \uD83D\uDCB0",
            "Hafta Sonu Fırsatları Başladı! ⭐",
            "Yeni Ürün Geldi! \uD83D\uDC7B",
            "Teklifleri Kaçırma! \uD83C\uDFAF",
            "Haftasonu İndirimleri Kaçırmayın! \uD83D\uDECD\uFE0F",
            "Sadece Bugüne Özel İndirimler! \uD83C\uDF81",
            "Yeni Koleksiyon Stokta! \uD83D\uDD25",
            "Tek Fırsat, Kaçırma! \uD83E\uDD16",
            "Büyük Yaz İndirimi Başladı! \uD83D\uDC7D",
            "En Yeni Ürünler Burada! \uD83C\uDF1E",
            "Son Gün, Kaçırmayın! \uD83D\uDD54",
            "Rüzgarı Hisset! \uD83D\uDC40"
        )
        return notificationTitles[random]
    }

    override fun getNotificationMessage(random: Int): String {
        val notificationMessages = listOf(
            "Soğuk kış günlerine özel indirimlerle alışverişe başla!",
            "Yeni yıla özel kampanyalar ve sürprizler seni bekliyor!",
            "Bugün sadece sizin için özel indirim fırsatları sizleri bekliyor. Hemen alışverişe başlayın!",
            "Bu hafta sonu alışverişin keyfini çıkar, indirimler kaçırılmaz!",
            "Yeni ürünümüz stoklarımıza geldi. Hemen keşfetmek için tıklayın!",
            "Kaçırılmayacak teklifler seni bekliyor!",
            "Haftasonu boyunca geçerli indirimlerimizi kaçırmayın. Alışverişin keyfini çıkarın!",
            "Bugüne özel indirimler ve kampanyalar sizi bekliyor. Acele edin, fırsatları kaçırmayın!",
            "Yeni koleksiyonumuzla tarzınızı yenileyin. Şimdi göz atın!",
            "Fırsatları kaçırma, en sevdiğin ürünler şimdi çok uygun fiyatlarla seni bekliyor!",
            "Hemen harekete geç! Yaz alışverişinizi yapmak için doğru zaman.",
            "En yeni ürünlerimizi keşfedin. Sürprizlerle dolu alışveriş deneyimi için tıklayın!",
            "Son gün, kaçırmayın! İndirimli ürünler ve kampanyalar için hemen alışverişe başlayın!",
            "Yenilenen koleksiyonlarla tarzını yeniden keşfet!"
        )
        return notificationMessages[random]
    }
}