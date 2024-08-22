package com.hasanalic.ecommerce.feature_checkout.presentation.address_screen.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.databinding.RecyclerItemAddressBinding
import com.hasanalic.ecommerce.feature_checkout.domain.model.Address

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

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

    inner class MyViewHolder(private val binding: RecyclerItemAddressBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, position: Int) {
            binding.textViewAddressTitle.text = address.addressTitle
            binding.textViewAddressDetail.text = address.addressDetail
            binding.radioButton.isChecked = address.isSelected
            binding.materialCardAddress.setOnClickListener {
                onCardClickListener?.let {
                    it(position, address.addressId)
                }
            }
            binding.radioButton.setOnClickListener {
                onCardClickListener?.let {
                    it(position, address.addressId)
                }
            }
        }
    }

    private var onCardClickListener: ((Int, String) -> Unit)? = null

    fun setOnCardClickListener(listener: (Int, String) -> Unit) {
        onCardClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemAddressBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(addressList[position], position)
    }
}