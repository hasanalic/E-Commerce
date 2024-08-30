package com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import com.hasanalic.ecommerce.databinding.RecyclerItemProductImageBinding
import com.hasanalic.ecommerce.core.utils.glide
import com.hasanalic.ecommerce.core.utils.placeHolderProgressBar

class OrderImageAdapter: RecyclerView.Adapter<OrderImageAdapter.MyViewHolder>() {

    private val diffUtil = object: DiffUtil.ItemCallback<OrderProductsEntity>() {
        override fun areItemsTheSame(oldItem: OrderProductsEntity, newItem: OrderProductsEntity): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: OrderProductsEntity, newItem: OrderProductsEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var productImageList: List<OrderProductsEntity>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemProductImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(productImage: OrderProductsEntity) {
            binding.image.glide(productImage.orderProductsProductImage, placeHolderProgressBar(binding.root.context))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemProductImageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return productImageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(productImageList[position])
    }
}