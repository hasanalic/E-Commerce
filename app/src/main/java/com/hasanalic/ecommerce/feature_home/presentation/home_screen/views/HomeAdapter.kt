package com.hasanalic.ecommerce.feature_home.presentation.home_screen.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemProductBinding
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.core.utils.glide
import com.hasanalic.ecommerce.core.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.core.utils.toCent

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
        fun bind(product: Product, position: Int) {
            binding.imageViewProduct.glide(product.productPhoto, placeHolderProgressBar(binding.root.context))
            binding.textViewProductBrand.text = product.productBrand
            binding.textViewProductDetail.text = product.productDetail
            binding.textViewProductPrice.text = "${product.productPriceWhole}.${product.productPriceCent.toCent()} TL"
            binding.textViewReviewCount.text = "(${product.productReviewCount})"
            binding.textViewRate.text = product.productRate.toString()
            setStarsByProductRate(product.productRate.toString())

            if (product.addedToFavorites) {
                binding.imageViewFavorite.setImageResource(R.drawable.favorite_orange)
            } else {
                binding.imageViewFavorite.setImageResource(R.drawable.favorite_empty)
            }

            binding.materialCardProductItem.strokeWidth = 1

            if (product.addedToShoppingCart) {
                binding.buttonAddCart.text = "Remove"
                binding.buttonAddCart.setBackgroundColor(binding.root.resources.getColor(R.color.font_third))
            } else {
                binding.buttonAddCart.text = "Add to Cart"
                binding.buttonAddCart.setBackgroundColor(binding.root.resources.getColor(R.color.color_primary))
            }

            setClickListeners(product, position)
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

        private fun setClickListeners(product: Product, position: Int) {
            binding.buttonAddCart.setOnClickListener {
                if (product.addedToShoppingCart) {
                    removeProductFromCartButtonClickListener?.let {
                        it(product.productId, position)
                    }
                } else {
                    addProductToCartButtonClickListener?.let {
                        it(product.productId, position)
                    }
                }
            }
            binding.imageViewFavorite.setOnClickListener {
                if (product.addedToFavorites) {
                    removeProductFromFavoritesClickListener?.let {
                        it(product.productId, position)
                    }
                } else {
                    addProductToFavoritesClickListener?.let {
                        it(product.productId, position)
                    }
                }
            }
            binding.materialCardProductItem.setOnClickListener {
                onProductClickListener?.let {
                    it(product.productId)
                }
            }
        }
    }

    private var addProductToCartButtonClickListener: ((String, Int) -> Unit)? = null
    private var removeProductFromCartButtonClickListener: ((String, Int) -> Unit)? = null
    private var onProductClickListener: ((String) -> Unit)? = null
    private var addProductToFavoritesClickListener: ((String, Int) -> Unit)? = null
    private var removeProductFromFavoritesClickListener: ((String, Int) -> Unit)? = null

    fun setAddProductToCartButtonClickListener(listener: (String, Int) -> Unit) {
        addProductToCartButtonClickListener = listener
    }

    fun setRemoveProductFromCartButtonClickListener(listener: (String, Int) -> Unit) {
        removeProductFromCartButtonClickListener = listener
    }

    fun setAddProductToFavoritesClickListener(listener: (String, Int) -> Unit) {
        addProductToFavoritesClickListener = listener
    }

    fun setRemoveProductFromFavoritesClickListener(listener: (String, Int) -> Unit) {
        removeProductFromFavoritesClickListener = listener
    }

    fun setOnProductClickListener(listener: (String) -> Unit) {
        onProductClickListener = listener
    }

    fun notifyDataSetChangedInAdapter() {
        notifyDataSetChanged()
    }

    fun notifyItemChangedInAdapter(position: Int) {
        notifyItemChanged(position)
    }

    fun notifyItemRemovedInAdapter(position: Int) {
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(products[position], position)
    }
}