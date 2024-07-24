package com.hasanalic.ecommerce.feature_location.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.databinding.RecyclerItemAddressLocationBinding
import com.hasanalic.ecommerce.core.domain.model.Address

class LocationAdapter: RecyclerView.Adapter<LocationAdapter.MyViewHolder>() {

    private val diffUtil = object: DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var addressList: List<Address>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemAddressLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (address: Address) {
            binding.textViewAddressTitle.text = address.addressTitle
            binding.textViewAddressDetail.text = address.addressDetail

            binding.materialCardDelete.setOnClickListener {
                onDeleteClickListener?.let {
                    it(address.addressId)
                }
            }
        }
    }

    private var onDeleteClickListener: ((String) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (String) -> Unit) {
        onDeleteClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemAddressLocationBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(addressList[position])
    }
}