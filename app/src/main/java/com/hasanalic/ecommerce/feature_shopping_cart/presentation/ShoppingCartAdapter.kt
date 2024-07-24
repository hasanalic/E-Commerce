package com.hasanalic.ecommerce.feature_shopping_cart.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.databinding.RecyclerItemShoppingCartBinding
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.utils.glide
import com.hasanalic.ecommerce.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.utils.toCent

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
        fun bind(shoppingCartItem: ShoppingCartItem) {
            binding.imageViewShoppingCartItemPhoto.glide(shoppingCartItem.shoppingCartItemPhoto, placeHolderProgressBar(binding.root.context))
            binding.textViewBrand.text = shoppingCartItem.shoppingCartItemBrand
            binding.textViewDetail.text = shoppingCartItem.shoppingCartItemDetail
            binding.textViewPrice.text = "${shoppingCartItem.shoppingCartItemPriceWhole}.${shoppingCartItem.shoppingCartItemPriceCent.toCent()} TL"
            binding.textViewQuantity.text = shoppingCartItem.shoppingCartItemQuantity.toString()

            setOnClickListeners(shoppingCartItem)
        }

        private fun setOnClickListeners(shoppingCartItem: ShoppingCartItem) {
            binding.textViewDecrease.setOnClickListener {
                onDecreaseButtonClickListener?.let {
                    it(shoppingCartItem.shoppingCartItemId)
                }
            }
            binding.textViewIncrease.setOnClickListener {
                onIncreaseButtonClickListener?.let {
                    it(shoppingCartItem.shoppingCartItemId)
                }
            }
            binding.materialCardDelete.setOnClickListener {
                onDeleteButtonClickListener?.let {
                    it(shoppingCartItem.shoppingCartItemId)
                }
            }
            binding.materialCardShoppinCartItem.setOnClickListener {
                onCardClickListener?.let {
                    it(shoppingCartItem.shoppingCartItemId)
                }
            }
        }
    }

    private var onCardClickListener: ((String) -> Unit)? = null
    private var onDecreaseButtonClickListener: ((String) -> Unit)? = null
    private var onIncreaseButtonClickListener: ((String) -> Unit)? = null
    private var onDeleteButtonClickListener: ((String) -> Unit)? = null

    fun setOnCardClickListener(listener: (String) -> Unit) {
        onCardClickListener = listener
    }
    fun setOnDecreaseButtonClickListener(listener: (String) -> Unit) {
        onDecreaseButtonClickListener = listener
    }
    fun setOnIncreaseButtonClickListener(listener: (String) -> Unit) {
        onIncreaseButtonClickListener = listener
    }
    fun setOnDeleteButtonClickListener(listener: (String) -> Unit) {
        onDeleteButtonClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemShoppingCartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return shoppingCartItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(shoppingCartItems[position])
    }
}