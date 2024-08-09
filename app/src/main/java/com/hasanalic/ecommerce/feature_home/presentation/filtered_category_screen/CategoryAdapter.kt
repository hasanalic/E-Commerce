package com.hasanalic.ecommerce.feature_home.presentation.filtered_category_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.databinding.RecyclerItemCategoryBinding
import com.hasanalic.ecommerce.feature_home.domain.model.Category

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var categoryList: List<Category>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.chip.text = item.category

            binding.chip.setOnClickListener {
                onChipClickListener?.let {
                    it(item.category)
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
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }
}