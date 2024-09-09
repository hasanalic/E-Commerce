package com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_CANCELLED
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_CARGO
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_DELIVERED
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_PREPARE
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_RECEIVED
import com.hasanalic.ecommerce.core.presentation.utils.OrderConstants.ORDER_RETURNED
import com.hasanalic.ecommerce.databinding.RecyclerItemOrderBinding
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.presentation.utils.OrderStatus
import com.hasanalic.ecommerce.core.utils.hide

class OrderAdapter: RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    private val diffUtil = object: DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var orderList: List<Order>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(val binding: RecyclerItemOrderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.textViewDate.text = order.orderDate
            binding.textViewCost.text = order.orderTotal
            binding.textViewCount.text = "${order.orderProductCount} product"

            if (order.orderStatus == ORDER_CANCELLED) {
                binding.textViewOrderStatus.text = order.orderStatus
                binding.imageViewCheck.setImageResource(R.drawable.cancel_red)
            } else if (order.orderStatus == ORDER_RETURNED) {
                binding.textViewOrderStatus.text = order.orderStatus
                binding.imageViewCheck.setImageResource(R.drawable.return_back)
            } else {
                val orderCurrentStatus = OrderStatus.getOrderCurrrentStatus(order.orderTimeStamp, System.currentTimeMillis())

                when(orderCurrentStatus) {
                    ORDER_RECEIVED -> {
                        binding.textViewOrderStatus.text = ORDER_RECEIVED
                        binding.imageViewCheck.hide()
                    }
                    ORDER_PREPARE -> {
                        binding.textViewOrderStatus.text = ORDER_PREPARE
                        binding.imageViewCheck.hide()
                    }
                    ORDER_CARGO -> {
                        binding.textViewOrderStatus.text = ORDER_CARGO
                        binding.imageViewCheck.hide()
                    }
                    ORDER_DELIVERED -> {
                        binding.textViewOrderStatus.text = ORDER_DELIVERED
                        binding.imageViewCheck.setImageResource(R.drawable.check)
                    }
                }
            }

            binding.materialCardViewOrder.setOnClickListener {
                onOrderClickListener?.let {
                    it(order.orderId)
                }
            }
        }
    }

    private var onOrderClickListener: ((String) -> Unit)? = null

    fun setOnOrderClickListener(listener: (String) -> Unit) {
        onOrderClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemOrderBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val orderProductsEntity = orderList[position]
        holder.bind(orderProductsEntity)

        // GridLayoutManager(holder.binding.root.context,4)
        val layoutManager = GridLayoutManager(holder.binding.root.context,5)
        layoutManager.initialPrefetchItemCount = orderProductsEntity.orderProductsList.size

        val orderImageAdapter = OrderImageAdapter()
        orderImageAdapter.productImageList = orderProductsEntity.orderProductsList

        holder.binding.recyclerViewProducts.layoutManager = layoutManager
        holder.binding.recyclerViewProducts.adapter = orderImageAdapter
        holder.binding.recyclerViewProducts.setRecycledViewPool(viewPool)
    }
}