package com.hasanalic.ecommerce.ui.checkout.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.databinding.RecyclerItemAddressBinding
import com.hasanalic.ecommerce.domain.model.Address

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
        fun bind(address: Address) {
            binding.textViewAddressTitle.text = address.addressTitle
            binding.textViewAddressDetail.text = address.addressDetail
            binding.radioButton.isChecked = address.isSelected
            binding.materialCardAddress.setOnClickListener {
                onCardClickListener?.let {
                    it(address.addressId)
                }
            }
            binding.radioButton.setOnClickListener {
                onCardClickListener?.let {
                    it(address.addressId)
                }
            }
        }
    }

    private var onCardClickListener: ((String) -> Unit)? = null

    fun setOnCardClickListener(listener: (String) -> Unit) {
        onCardClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.MyViewHolder {
        return MyViewHolder(RecyclerItemAddressBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: AddressAdapter.MyViewHolder, position: Int) {
        holder.bind(addressList[position])
    }
}