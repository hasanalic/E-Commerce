package com.hasanalic.ecommerce.feature_notification.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.feature_notification.data.local.entity.NotificationEntity
import com.hasanalic.ecommerce.databinding.RecyclerItemNotificationBinding

class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<NotificationEntity>() {
        override fun areItemsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var notificationList: List<NotificationEntity>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemNotificationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationEntity) {
            binding.notificationTitle.text = notification.notificationTitle
            binding.notificationContent.text = notification.notificationContent
            binding.notificationTime.text = calculateTime(notification.notificationTime)
        }

        private fun calculateTime(notificationTime: Long): String {
            val currentTime = System.currentTimeMillis()
            val passedTime = currentTime - notificationTime

            val minute = passedTime / (60 * 1000) % 60
            val hour = passedTime / (60 * 60 * 1000) % 24
            val day = passedTime / (24 * 60 * 60 * 1000)

            return when {
                day > 0 -> "$day days ago"
                hour > 0 -> "$hour hour ago"
                minute > 0 -> "$minute minute ago"
                else -> "Now"
            }
        }
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemNotificationBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }
}