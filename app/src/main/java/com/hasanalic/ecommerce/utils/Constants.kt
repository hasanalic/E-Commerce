package com.hasanalic.ecommerce.utils

object Constants {
    const val REQUEST_CODR = 100
    const val REQUEST_CHECK_SETTINGS = 1001
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val ANOMIM_USER_ID = "-100"

    // ALARMS
    const val ADDS_ALARM_REQUEST_CODE = 10006
    const val ADDS_ALARM_INTERVAL = 60 * 1000 * 10  // 10 dakika
    const val ADDS_ALARM_INTERVAL_TEST = 60 * 1000  // 1 dakika

    const val CART_ALARM_REQUEST_CODE = 10009
    const val CART_ALARM_INTERVAL = 60 * 1000 * 12  // 12 dakika
    const val CART_ALARM_INTERVAL_TEST = 60 * 1000 * 5 // 1 dakika

    // LOCAL NOTIFICATION
    // 3D Secure
    const val CHANNEL_ID = "1864513179"
    const val NOTIFICATION_ID = 101
    const val CHANNEL_NAME = "Random Number Channel"
    const val CHANNEL_DESCRIPTION_TEXT = "Channel for random number notifications"

    // Adds
    const val CHANNEL_ID_ADDS = "adds_channel"
    const val CHANNEL_NAME_ADDS = "Kampanyalar"
    const val NOTIFICATION_ID_ADDS = 501
    const val INTERVAL_MILLIS_ADDS = 3 * 60 * 1000

    // Shopping Cart
    const val CHANNEL_ID_SHOPPING_CART = "shopping_cart_channel"
    const val CHANNEL_NAME_SHOPPING_CART = "Alışveriş Sepeti"
    const val NOTIFICATION_ID_SHOPPING_CART = 980
    const val INTERVAL_MILLIS_SHOPPING_CART = 2 * 60 * 1000

    // Order Status
    const val ORDER_RECEIVED = "Sipariş Alındı"
    const val ORDER_PREPARE = "Hazırlanıyor"
    const val ORDER_CARGO = "Kargoya Verildi"
    const val ORDER_DELIVERED = "Teslim Edildi"
    const val ORDER_CANCELLED = "İptal Edildi"
    const val ORDER_RETURNED = "İade Edildi"

    // Payment Type
    const val BANK_OR_CREDIT_CARD = "Banka/ Kredi Kartı"
    const val AT_DOOR = "Kapıda Ödeme"
}


/*
    CTRL + ALT + T          ->      Seçili kodları sarmalamak.
    CTRL + SHIFT + (SAYI)   ->      İşaretlenen satıra "sayı" değerinde bookmark ekler.
    CTRL + (SAYI)           ->      Değeri "sayı" olan bookmark'a gider.
    SHIFT + F11             ->      Bookmark listesini gösterir.
    Live Templates          ->      Sık kullanılan kod parçaları "Custom" altına yazılabilir.
 */