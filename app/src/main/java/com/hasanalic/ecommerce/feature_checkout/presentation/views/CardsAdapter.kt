package com.hasanalic.ecommerce.feature_checkout.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.data.dto.PaymentEntity
import com.hasanalic.ecommerce.databinding.RecyclerItemCardBinding
import com.hasanalic.ecommerce.utils.maskCreditCard

class CardsAdapter: RecyclerView.Adapter<CardsAdapter.MyViewHolder>()  {

    private val diffUtil = object: DiffUtil.ItemCallback<PaymentEntity>() {
        override fun areItemsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var cardList: List<PaymentEntity>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: RecyclerItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(card: PaymentEntity) {
            val censoredNumber = card.paymentCardNumber!!.maskCreditCard()
            binding.textViewCardNumber.text = censoredNumber

            binding.materialCardBankOrCreditCard.setOnClickListener {
                onCardClickListener?.let {
                    it(card.paymentId.toString())
                }
            }
        }

        private fun maskCreditCard(input: String): String {
            val firstPart = input.substring(0, 4).padEnd(4, '*')

            val lastPart = input.substring(input.length - 2).padStart(2, '*')

            val middlePart = input.substring(4, input.length - 2)
                .chunked(4) { "****" }
                .joinToString(separator = " ")

            return "$firstPart $middlePart $lastPart"
        }
    }

    private var onCardClickListener: ((String) -> Unit)? = null

    fun setOnCardClickLisstener(listener: (String) -> Unit) {
        onCardClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(cardList[position])
    }
}