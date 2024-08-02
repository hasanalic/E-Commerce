package com.hasanalic.ecommerce.feature_favorite.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemFavoriteBinding
import com.hasanalic.ecommerce.feature_favorite.domain.model.FavoriteProduct
import com.hasanalic.ecommerce.utils.glide
import com.hasanalic.ecommerce.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.utils.toCent

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<FavoriteProduct>() {
        override fun areItemsTheSame(oldItem: FavoriteProduct, newItem: FavoriteProduct): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: FavoriteProduct, newItem: FavoriteProduct): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var favoriteProducts: List<FavoriteProduct>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteProduct: FavoriteProduct, position: Int) {
            binding.imageViewProduct.glide(favoriteProduct.photo, placeHolderProgressBar(binding.root.context))
            binding.textViewProductBrand.text = favoriteProduct.brand
            binding.textViewProductDetail.text = favoriteProduct.detail
            binding.textViewProductPrice.text = "${favoriteProduct.priceWhole}.${favoriteProduct.priceCent.toCent()} TL"
            binding.textViewRate.text = favoriteProduct.rate.toString()
            setStarsByProductRate(favoriteProduct.rate.toString())
            binding.textViewReviewCount.text = "(${favoriteProduct.reviewCount})"
            binding.imageViewFavorite.setImageResource(R.drawable.favorite_orange)

            if (favoriteProduct.addedToShoppingCart) {
                binding.buttonCart.text = "Kaldır"
                binding.buttonCart.setBackgroundColor(binding.root.resources.getColor(R.color.font_third))
            } else {
                binding.buttonCart.text = "Sepete Ekle"
                binding.buttonCart.setBackgroundColor(binding.root.resources.getColor(R.color.color_primary))
            }

            setClickListeners(favoriteProduct, position)
        }

        private fun setStarsByProductRate(productRate: String) {
            val productRateList = productRate.split(".")
            val productRateBefore = productRateList[0].toInt()
            val productRateAfter = productRateList[1].toInt()
            if (productRateBefore == 3) {
                if (productRateAfter == 0) {
                    binding.imageViewStarFour.setImageResource(R.drawable.star_empty)
                    binding.imageViewStarFive.setImageResource(R.drawable.star_empty)
                } else {
                    binding.imageViewStarFour.setImageResource(R.drawable.star_half)
                    binding.imageViewStarFive.setImageResource(R.drawable.star_empty)
                }
            } else if (productRateBefore == 4) {
                if (productRateAfter == 0) {
                    binding.imageViewStarFour.setImageResource(R.drawable.star)
                    binding.imageViewStarFive.setImageResource(R.drawable.star_empty)
                } else {
                    binding.imageViewStarFour.setImageResource(R.drawable.star)
                    binding.imageViewStarFive.setImageResource(R.drawable.star_half)
                }
            }
        }

        private fun setClickListeners(favoriteProduct: FavoriteProduct, position: Int) {
            binding.buttonCart.setOnClickListener {
                if (favoriteProduct.addedToShoppingCart) {
                    onRemoveFromCartButtonClickListener?.let {
                        it(favoriteProduct.productId, position)
                    }
                } else {
                    onAddCartButtonClickListener?.let {
                        it(favoriteProduct.productId, position)
                    }
                }
            }

            binding.imageViewFavorite.setOnClickListener {
                onRemoveFromFavoriteClickListener?.let {
                    it(favoriteProduct.productId, position)
                }
            }
            binding.materialCardProductItem.setOnClickListener {
                onCardClickListener?.let {
                    it(favoriteProduct.productId)
                }
            }
        }
    }

    private var onCardClickListener: ((String) -> Unit)? = null
    private var onRemoveFromCartButtonClickListener: ((String, Int) -> Unit)? = null
    private var onAddCartButtonClickListener: ((String, Int) -> Unit)? = null
    private var onRemoveFromFavoriteClickListener: ((String, Int) -> Unit)? = null

    fun setOnCardClickListener(listener: (String) -> Unit) {
        onCardClickListener = listener
    }
    fun setOnAddCartButtonClickListener(listener: (String, Int) -> Unit) {
        onAddCartButtonClickListener = listener
    }
    fun setOnRemoveFromCartButtonClickListener(listener: (String, Int) -> Unit) {
        onRemoveFromCartButtonClickListener = listener
    }
    fun setOnRemoveFromFavoriteClickListener(listener: (String, Int) -> Unit) {
        onRemoveFromFavoriteClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return favoriteProducts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(favoriteProducts[position], position)
    }
}