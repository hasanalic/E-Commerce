package com.hasanalic.ecommerce.feature_favorite.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemFavoriteBinding
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.utils.glide
import com.hasanalic.ecommerce.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.utils.toCent

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var products: List<Product>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.imageViewProduct.glide(product.productPhoto, placeHolderProgressBar(binding.root.context))
            binding.textViewProductBrand.text = product.productBrand
            binding.textViewProductDetail.text = product.productDetail
            binding.textViewProductPrice.text = "${product.productPriceWhole}.${product.productPriceCent.toCent()} TL"
            binding.textViewRate.text = product.productRate.toString()
            setStarsByProductRate(product.productRate.toString())
            binding.textViewReviewCount.text = "(${product.productReviewCount})"

            if (product.addedToFavorites) {
                binding.imageViewFavorite.setImageResource(R.drawable.favorite_orange)
            } else {
                binding.imageViewFavorite.setImageResource(R.drawable.favorite_border_orange)
            }
            if (product.addedToShoppingCart) {
                binding.buttonAddCart.text = "Kaldır"
                binding.buttonAddCart.setBackgroundColor(binding.root.resources.getColor(R.color.font_third))
            } else {
                binding.buttonAddCart.text = "Sepete Ekle"
                binding.buttonAddCart.setBackgroundColor(binding.root.resources.getColor(R.color.color_primary))
            }

            setClickListeners(product)
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

        private fun setClickListeners(product: Product) {
            binding.buttonAddCart.setOnClickListener {
                onCartButtonClickListener?.let {
                    it(product.productId)
                }
            }
            binding.imageViewFavorite.setOnClickListener {
                onFavoriteClickListener?.let {
                    it(product.productId)
                }
            }
            binding.materialCardProductItem.setOnClickListener {
                onCardClickListener?.let {
                    it(product.productId)
                }
            }
        }
    }

    private var onCardClickListener: ((String) -> Unit)? = null
    private var onCartButtonClickListener: ((String) -> Unit)? = null
    private var onFavoriteClickListener: ((String) -> Unit)? = null

    fun setOnCardClickListener(listener: (String) -> Unit) {
        onCardClickListener = listener
    }
    fun setOnCartButtonClickListener(listener: (String) -> Unit) {
        onCartButtonClickListener = listener
    }
    fun setOnFavoriteClickListener(listener: (String) -> Unit) {
        onFavoriteClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(products[position])
    }
}