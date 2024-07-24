package com.hasanalic.ecommerce.feature_home.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemProductBinding
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.utils.glide
import com.hasanalic.ecommerce.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.utils.toCent

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

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

    inner class MyViewHolder(private val binding: RecyclerItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.imageViewProduct.glide(product.productPhoto, placeHolderProgressBar(binding.root.context))
            binding.textViewProductBrand.text = product.productBrand
            binding.textViewProductDetail.text = product.productDetail
            binding.textViewProductPrice.text = "${product.productPriceWhole}.${product.productPriceCent.toCent()} TL"
            binding.textViewRate.text = product.productRate.toString()
            setStarsByProductRate(product.productRate.toString())
            binding.textViewReviewCount.text = "(${product.productReviewCount})"

            if (product.addedToCompare) {
                binding.checkboxCompare.isChecked = true
                binding.checkboxCompare.text = "Karşılaştırmadan çıkar"
            } else {
                binding.checkboxCompare.isChecked = false
                binding.checkboxCompare.text = "Karşılaştırmaya ekle"
            }
            if (product.addedToFavorites) {
                binding.imageViewFavorite.setImageResource(R.drawable.favorite_orange)
            } else {
                binding.imageViewFavorite.setImageResource(R.drawable.favorite_border_orange)
            }

            binding.materialCardProductItem.strokeWidth = 1

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
                if (product.addedToShoppingCart) {
                    removeCartButtonClickListener?.let {
                        it(product.productId)
                    }
                } else {
                    addCartButtonClickListener?.let {
                        it(product.productId)
                    }
                }
            }
            binding.imageViewFavorite.setOnClickListener {
                if (product.addedToFavorites) {
                    removeFavoriteClickListener?.let {
                        it(product.productId)
                    }
                } else {
                    addFavoriteClickListener?.let {
                        it(product.productId)
                    }
                }
            }
            binding.checkboxCompare.setOnClickListener {
                onCompareClickListener?.let {
                    it(product.productId)
                }
            }
            binding.materialCardProductItem.setOnClickListener {
                onProductClickListener?.let {
                    it(product.productId)
                }
            }
        }
    }

    private var addCartButtonClickListener: ((String) -> Unit)? = null
    private var removeCartButtonClickListener: ((String) -> Unit)? = null
    private var onProductClickListener: ((String) -> Unit)? = null
    private var addFavoriteClickListener: ((String) -> Unit)? = null
    private var removeFavoriteClickListener: ((String) -> Unit)? = null
    private var onCompareClickListener: ((String) -> Unit)? = null

    fun setAddCartButtonClickListener(listener: (String) -> Unit) {
        addCartButtonClickListener = listener
    }
    fun setRemoveCartButtonClickListener(listener: (String) -> Unit) {
        removeCartButtonClickListener = listener
    }
    fun setOnProductClickListener(listener: (String) -> Unit) {
        onProductClickListener = listener
    }
    fun setAddFavoriteClickListener(listener: (String) -> Unit) {
        addFavoriteClickListener = listener
    }
    fun removeFavoriteClickListener(listener: (String) -> Unit) {
        removeFavoriteClickListener = listener
    }
    fun setOnCompareClickListener(listener: (String) -> Unit) {
        onCompareClickListener = listener
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(products[position])
    }
}