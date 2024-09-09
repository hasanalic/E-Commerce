package com.hasanalic.ecommerce.core.presentation.utils

import android.os.Build
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TimeAndDate {
    companion object {
        fun getLocalDate(dateFormat: String): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0 (API 26) and above
                val currentDate = java.time.LocalDate.now()
                val formatter =
                    java.time.format.DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault())
                return currentDate.format(formatter)
            } else {
                // Android 7 (API 24) and below
                val currentDate = Date()
                val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
                return formatter.format(currentDate)
            }
        }

        fun getTime(): String {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            return "$hour:$minute"
        }

        fun estimatedDeliveryTime(dateFormat: String, orderDate: String): String {
            val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            val date = dateFormat.parse(orderDate)

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_YEAR, 2)
            val newDate = calendar.time

            val newDateFormat = SimpleDateFormat("d MMMM", Locale("tr", "TR"))
            val formattedDate = newDateFormat.format(newDate)

            return formattedDate
        }

        fun timeStamp(): String {
            val timeStamp = Timestamp(System.currentTimeMillis())
            val sdf = SimpleDateFormat("HH:mm")
            val time = sdf.format(Date(timeStamp.time))

            return time.toString()
        }
    }
}