package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.databinding.RecyclerItemShoppingCartBinding
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.core.utils.glide
import com.hasanalic.ecommerce.core.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.core.utils.toCent

class ShoppingCartAdapter: RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ShoppingCartItem>() {
        override fun areItemsTheSame(oldItem: ShoppingCartItem, newItem: ShoppingCartItem): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: ShoppingCartItem, newItem: ShoppingCartItem): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var shoppingCartItems: List<ShoppingCartItem>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemShoppingCartBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(shoppingCartItem: ShoppingCartItem, position: Int) {
            binding.imageViewShoppingCartItemPhoto.glide(shoppingCartItem.photo, placeHolderProgressBar(binding.root.context))
            binding.textViewBrand.text = shoppingCartItem.brand
            binding.textViewDetail.text = shoppingCartItem.detail
            binding.textViewPrice.text = "${shoppingCartItem.priceWhole}.${shoppingCartItem.priceCent.toCent()} TL"
            binding.textViewQuantity.text = shoppingCartItem.quantity.toString()

            setOnClickListeners(shoppingCartItem, position)
        }

        private fun setOnClickListeners(shoppingCartItem: ShoppingCartItem, position: Int) {
            binding.textViewDecrease.setOnClickListener {
                onDecreaseButtonClickListener?.let {
                    it(shoppingCartItem.productId, shoppingCartItem.quantity, position)
                }
            }
            binding.textViewIncrease.setOnClickListener {
                onIncreaseButtonClickListener?.let {
                    it(shoppingCartItem.productId, shoppingCartItem.quantity, position)
                }
            }
            binding.materialCardDelete.setOnClickListener {
                onDeleteButtonClickListener?.let {
                    it(shoppingCartItem.productId, position)
                }
            }
            binding.materialCardShoppinCartItem.setOnClickListener {
                onCardClickListener?.let {
                    it(shoppingCartItem.productId)
                }
            }
        }
    }

    private var onCardClickListener: ((String) -> Unit)? = null
    private var onDecreaseButtonClickListener: ((String, Int, Int) -> Unit)? = null
    private var onIncreaseButtonClickListener: ((String, Int, Int) -> Unit)? = null
    private var onDeleteButtonClickListener: ((String, Int) -> Unit)? = null

    fun setOnCardClickListener(listener: (String) -> Unit) {
        onCardClickListener = listener
    }
    fun setOnDecreaseButtonClickListener(listener: (String, Int, Int) -> Unit) {
        onDecreaseButtonClickListener = listener
    }
    fun setOnIncreaseButtonClickListener(listener: (String, Int, Int) -> Unit) {
        onIncreaseButtonClickListener = listener
    }
    fun setOnDeleteButtonClickListener(listener: (String, Int) -> Unit) {
        onDeleteButtonClickListener = listener
    }

    fun notifyDataSetChangedInAdapter() {
        notifyDataSetChanged()
    }

    fun notifyItemChangedInAdapter(position: Int) {
        notifyItemChanged(position)
    }

    fun notifyItemRemovedInAdapter(position: Int) {
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemShoppingCartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return shoppingCartItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(shoppingCartItems[position], position)
    }
}