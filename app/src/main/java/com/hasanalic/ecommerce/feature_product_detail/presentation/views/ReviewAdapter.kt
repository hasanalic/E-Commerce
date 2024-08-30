package com.hasanalic.ecommerce.feature_product_detail.presentation.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.RecyclerItemReviewBinding
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.core.utils.glide
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.placeHolderProgressBar

class ReviewAdapter: RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {

    private val diffUtil = object: DiffUtil.ItemCallback<ReviewEntity>() {
        override fun areItemsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: ReviewEntity, newItem: ReviewEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var reviewList: List<ReviewEntity>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    inner class MyViewHolder(private val binding: RecyclerItemReviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewEntity) {
            binding.textViewName.text = review.reviewName
            binding.textViewDate.text = review.reviewDate
            binding.textViewReviewTitle.text = review.reviewTitle
            binding.textViewReviewContent.text = review.reviewContent
            binding.imageViewProfilePhoto.glide(review.reviewProfilePhoto,
                placeHolderProgressBar(binding.root.context)
            )
            review.reviewProductPhoto?.let {
                binding.imageViewProductPhoto.glide(it, placeHolderProgressBar(binding.root.context))
            }?: binding.imageViewProductPhoto.hide()
            if (review.reviewProductPhoto == null) {
                binding.imageViewProductPhoto.hide()
            } else {
                binding.imageViewProductPhoto.glide(review.reviewProductPhoto,
                    placeHolderProgressBar(binding.root.context)
                )
            }
            when(review.reviewStar) {
                1 -> {
                    binding.imageViewStarTwo.setImageResource(R.drawable.star_empty)
                    binding.imageViewStarThree.setImageResource(R.drawable.star_empty)
                    binding.imageViewStarFour.setImageResource(R.drawable.star_empty)
                    binding.imageViewStarFive.setImageResource(R.drawable.star_empty)
                }
                2 -> {
                    binding.imageViewStarThree.setImageResource(R.drawable.star_empty)
                    binding.imageViewStarFour.setImageResource(R.drawable.star_empty)
                    binding.imageViewStarFive.setImageResource(R.drawable.star_empty)
                }
                3 -> {
                    binding.imageViewStarFour.setImageResource(R.drawable.star_empty)
                    binding.imageViewStarFive.setImageResource(R.drawable.star_empty)
                }
                4 -> {
                    binding.imageViewStarFive.setImageResource(R.drawable.star_empty)
                }
            }
        }
    }

    fun notifyChanges() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemReviewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }
}