package com.hasanalic.ecommerce.feature_product_detail.presentation.views

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentProductDetailBinding
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_product_detail.domain.model.ProductDetail
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailState
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailViewModel
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.glide
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toCent
import com.hasanalic.ecommerce.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment: Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductDetailViewModel

    private var productId: String? = null

    private val reviewAdapter by lazy {
        ReviewAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scrollViewProductDetail.isFocusableInTouchMode = true
        binding.scrollViewProductDetail.fullScroll(View.FOCUS_UP)
        binding.scrollViewProductDetail.smoothScrollTo(0,0)

        viewModel = ViewModelProvider(requireActivity())[ProductDetailViewModel::class.java]

        arguments?.let {
            productId = it.getString(requireActivity().getString(R.string.product_id))
            viewModel.getProductDetailAndReviews(productId ?: "")
        }

        setupListeners(view)

        setupRecyclerView()

        setupObservers()
    }

    private fun setupListeners(view: View) {
        binding.toolBarProductDetail.setNavigationOnClickListener {
            requireActivity().finish()
        }

        view.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                requireActivity().finish()
            }
            false
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewProductDetail.adapter = reviewAdapter
        binding.recyclerViewProductDetail.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewProductDetail.addItemDecoration(ItemDecoration(40,40,30))
    }

    private fun setupObservers() {
        viewModel.productDetailState.observe(viewLifecycleOwner) { state ->
            handleProductDetailState(state)
        }
    }

    private fun handleProductDetailState(state: ProductDetailState) {
        if (state.isLoading) {
            binding.progressBarProductDetail.show()
        } else {
            binding.progressBarProductDetail.hide()
        }

        if (state.shouldUserMoveToAuthActivity) {
            navigateToAuthActivity()
        }

        state.productDetail?.let {
            setupProductDetail(it)
        }

        state.reviewList.let {
            if (it.isEmpty()) {
                binding.textViewReviewContentCount.text = "Comments (0)"
                binding.emptyReview.show()
            } else {
                reviewAdapter.reviewList = it
                binding.textViewReviewContentCount.text = "Comments (${it.size})"
                binding.emptyReview.hide()
            }
        }

        state.dataError?.let {
            TODO()
        }

        state.actionError?.let {
            toast(requireContext(), it, false)
        }
    }

    private fun setupProductDetail(productDetail: ProductDetail) {
        binding.toolBarProductDetail.title = "Categories/ ${productDetail.productCategory}"
        binding.imageViewProductPhoto.glide(productDetail.productPhoto, placeHolderProgressBar(binding.root.context))
        binding.textViewProductBrand.text = productDetail.productBrand
        binding.textViewProductDetail.text = productDetail.productDetail
        binding.textViewPrice.text = "${productDetail.productPriceWhole}.${productDetail.productPriceCent.toCent()} TL"
        binding.textViewRate.text = productDetail.productRate.toString()
        binding.textViewReviewCount.text = "(${productDetail.productReviewCount})"
        binding.textViewShipping.text = productDetail.productShipping
        binding.textViewStore.text = productDetail.productStore
        binding.textViewStoreRate.text = productDetail.productStoreRate

        if (productDetail.productRate < 4.0) {
            binding.imageViewStar.setImageResource(R.drawable.star_half)
        } else {
            binding.imageViewStar.setImageResource(R.drawable.star)
        }

        if (productDetail.addedToFavorites) {
            binding.imageViewFavorite.setImageResource(R.drawable.favorite_orange)
        } else {
            binding.imageViewFavorite.setImageResource(R.drawable.favorite_empty)
        }

        if (productDetail.addedToShoppingCart) {
            binding.buttonAddCart.text = "Remove"
            binding.buttonAddCart.setBackgroundColor(binding.root.resources.getColor(R.color.font_third))
        } else {
            binding.buttonAddCart.text = "Add to Cart"
            binding.buttonAddCart.setBackgroundColor(binding.root.resources.getColor(R.color.color_primary))
        }

        binding.imageViewShare.setOnClickListener {
            val link = "https://www.eticaretuygulamasi.com/urun_detay?id=${productDetail.productId}"
            shareContent("title", link)
        }

        binding.imageViewFavorite.setOnClickListener {
            if (productDetail.addedToFavorites) {
                viewModel.removeProductFromFavoritesIfUserLoggedIn(productDetail.productId)
            } else {
                viewModel.addProductToFavoritesIfUserLoggedIn(productDetail.productId)
            }
        }

        binding.buttonAddCart.setOnClickListener {
            if (productDetail.addedToShoppingCart) {
                viewModel.removeProductFromCart(productDetail.productId)
            } else {
                viewModel.addProductToCart(productDetail.productId)
            }
        }
    }

    private fun shareContent(title: String, url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun navigateToAuthActivity() {
        toast(requireContext(),"You must log in to use your favorite operations.",false)
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}