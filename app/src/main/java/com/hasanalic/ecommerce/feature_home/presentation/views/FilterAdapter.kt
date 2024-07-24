package com.hasanalic.ecommerce.feature_home.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemFilterBinding
import com.hasanalic.ecommerce.domain.model.Chip

class FilterAdapter: RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Chip>() {
        override fun areItemsTheSame(oldItem: Chip, newItem: Chip): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Chip, newItem: Chip): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var chipList: List<Chip>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemFilterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Chip) {
            binding.chip.text = item.value
            if (item.isSelected) {
                binding.chip.chipBackgroundColor = ContextCompat.getColorStateList(binding.root.context,R.color.color_primary)
                binding.chip.setTextColor(binding.root.context.resources.getColor(R.color.white))
            } else {
                binding.chip.chipBackgroundColor = ContextCompat.getColorStateList(binding.root.context,R.color.color_light_grey)
                binding.chip.setTextColor(binding.root.context.resources.getColor(R.color.font_secondary))
            }

            binding.chip.setOnClickListener {
                onChipClickListener?.let {
                    it(item.value)
                }
            }
        }
    }

    private var onChipClickListener: ((String) -> Unit)? = null

    fun setOnChipClickListener(listener: (String) -> Unit) {
        onChipClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemFilterBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return chipList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(chipList[position])
    }
}