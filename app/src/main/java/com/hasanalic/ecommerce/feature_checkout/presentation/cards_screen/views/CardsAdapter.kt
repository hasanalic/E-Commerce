package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.databinding.RecyclerItemCardBinding
import com.hasanalic.ecommerce.core.utils.maskCreditCard

class CardsAdapter: RecyclerView.Adapter<CardsAdapter.MyViewHolder>()  {

    private val diffUtil = object: DiffUtil.ItemCallback<CardEntity>() {
        override fun areItemsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var cardList: List<CardEntity>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: RecyclerItemCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(card: CardEntity) {
            val censoredNumber = card.cardNumber!!.maskCreditCard()
            binding.textViewCardNumber.text = censoredNumber

            binding.materialCardBankOrCreditCard.setOnClickListener {
                onCardClickListener?.let {
                    it(card.cardId.toString())
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