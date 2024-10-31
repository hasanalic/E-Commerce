package com.hasanalic.ecommerce.feature_location.presentation.views

import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemAddressLocationBinding
import com.hasanalic.ecommerce.feature_location.domain.model.Location
import com.hasanalic.ecommerce.feature_location.presentation.base.BaseAdapter

class LocationAdapter(
    private val list: List<Location>,
    private val locationListener: LocationListener
) : BaseAdapter<RecyclerItemAddressLocationBinding, Location>(list) {

    override val layoutId: Int = R.layout.recycler_item_address_location

    override fun bind(binding: RecyclerItemAddressLocationBinding, item: Location) {
        binding.apply {
            location = item
            listener = locationListener
            executePendingBindings()
        }
    }
}

interface LocationListener {
    fun onDeleteClicked(location: Location)
}



/*
class LocationAdapter: RecyclerView.Adapter<LocationAdapter.MyViewHolder>() {

    private val diffUtil = object: DiffUtil.ItemCallback<AddressEntity>() {
        override fun areItemsTheSame(oldItem: AddressEntity, newItem: AddressEntity): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: AddressEntity, newItem: AddressEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var addressList: List<AddressEntity>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemAddressLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (address: AddressEntity, position: Int) {
            binding.textViewAddressTitle.text = address.addressTitle
            binding.textViewAddressDetail.text = address.addressDetail

            binding.materialCardDelete.setOnClickListener {
                onDeleteClickListener?.let {
                    it(address.addressId, position)
                }
            }
        }
    }

    private var onDeleteClickListener: ((Int, Int) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Int, Int) -> Unit) {
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
        holder.bind(addressList[position], position)
    }
}

 */