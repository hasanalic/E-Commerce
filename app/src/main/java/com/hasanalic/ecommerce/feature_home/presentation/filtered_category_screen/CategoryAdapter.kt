package com.hasanalic.ecommerce.feature_home.presentation.filtered_category_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.databinding.RecyclerItemCategoryBinding
import com.hasanalic.ecommerce.feature_home.domain.model.Chip

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

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

    inner class MyViewHolder(private val binding: RecyclerItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Chip) {
            binding.chip.text = item.value

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
        return MyViewHolder(RecyclerItemCategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return chipList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(chipList[position])
    }
}