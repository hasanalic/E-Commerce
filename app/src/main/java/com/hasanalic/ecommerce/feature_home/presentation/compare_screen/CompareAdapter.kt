package com.hasanalic.ecommerce.feature_home.presentation.compare_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemComparedBinding
import com.hasanalic.ecommerce.feature_home.domain.model.ComparedProduct
import com.hasanalic.ecommerce.core.utils.glide
import com.hasanalic.ecommerce.core.utils.placeHolderProgressBar

class CompareAdapter: RecyclerView.Adapter<CompareAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ComparedProduct>() {
        override fun areItemsTheSame(oldItem: ComparedProduct, newItem: ComparedProduct): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: ComparedProduct, newItem: ComparedProduct): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var comparedProducts: List<ComparedProduct>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemComparedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comparedProduct: ComparedProduct) {
            binding.imageViewProduct.glide(comparedProduct.productPhoto, placeHolderProgressBar(binding.root.context))
            binding.brand.text = comparedProduct.productBrand
            binding.detail.text = comparedProduct.productDetail
            binding.point.text = comparedProduct.productRate.toString()
            binding.price.text = "${comparedProduct.productPriceWhole},${comparedProduct.productPriceCent} TL"
            binding.store.text = comparedProduct.productStore
            binding.textViewStoreRate.text = comparedProduct.productStoreRate

            if (comparedProduct.productRate < 4.0) {
                binding.star.setImageResource(R.drawable.star_half)
            } else {
                binding.star.setImageResource(R.drawable.star)
            }

            binding.root.setOnClickListener {
                onComparedProductListener?.let {
                    it(comparedProduct.productId)
                }
            }
        }
    }

    private var onComparedProductListener: ((String) -> Unit)? = null

    fun setOnComparedProductListener(listener: (String) -> Unit) {
        onComparedProductListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemComparedBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return comparedProducts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(comparedProducts[position])
    }
}