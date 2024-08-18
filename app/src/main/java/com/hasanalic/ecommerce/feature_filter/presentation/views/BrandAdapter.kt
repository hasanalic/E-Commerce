package com.hasanalic.ecommerce.feature_filter.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemFilterBinding
import com.hasanalic.ecommerce.feature_home.domain.model.Brand

class BrandAdapter: RecyclerView.Adapter<BrandAdapter.MyViewHolder>() {
    private val diffUtil = object : DiffUtil.ItemCallback<Brand>() {
        override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var brandList: List<Brand>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemFilterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Brand, position: Int) {
            binding.chip.text = item.brand
            if (item.isSelected) {
                binding.chip.chipBackgroundColor = ContextCompat.getColorStateList(binding.root.context,
                    R.color.color_primary)
                binding.chip.setTextColor(binding.root.context.resources.getColor(R.color.white))
            } else {
                binding.chip.chipBackgroundColor = ContextCompat.getColorStateList(binding.root.context,
                    R.color.color_light_grey)
                binding.chip.setTextColor(binding.root.context.resources.getColor(R.color.font_secondary))
            }

            binding.chip.setOnClickListener {
                onBrandClickListener?.let {
                    it(item.brand, position)
                }
            }
        }
    }

    private var onBrandClickListener: ((String, Int) -> Unit)? = null

    fun setOnBrandClickListener(listener: (String, Int) -> Unit) {
        onBrandClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemFilterBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return brandList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(brandList[position], position)
    }
}