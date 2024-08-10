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
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailViewModel
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.ItemDecoration
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.glide
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.placeHolderProgressBar
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toCent
import com.hasanalic.ecommerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment: Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductDetailViewModel

    private var productId: String? = null

    //private lateinit var auth: FirebaseAuth
    private var userId: String = Constants.ANOMIM_USER_ID

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

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            userId = it.uid
        }

         */

        binding.toolBarProductDetail.setNavigationOnClickListener {
            requireActivity().finish()
        }

        view.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                requireActivity().finish()
            }
            false
        }

        viewModel = ViewModelProvider(requireActivity())[ProductDetailViewModel::class.java]

        arguments?.let {
            productId = it.getString(requireActivity().getString(R.string.product_id))
            viewModel.getProduct(userId,productId ?: "")
            viewModel.getReviews(productId ?: "")
        }

        setRecyclerView()

        observer()
    }

    private fun observer() {
        viewModel.stateProduct.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    setProductView(it.data!!)
                    binding.progressBarProductDetail.hide()
                }
                is Resource.Loading -> {
                    binding.progressBarProductDetail.show()
                }
                is Resource.Error -> {
                    binding.progressBarProductDetail.hide()
                }
            }
        }

        viewModel.stateReviewList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarProductDetail.hide()
                    val reviewList = it.data?.toList()

                    if (reviewList.isNullOrEmpty()) {
                        reviewAdapter.reviewList = listOf()
                        // değerlendirme yok yazısı göster
                        binding.textViewReviewContentCount.text = "Değerlendirme (0)"
                        binding.emptyReview.show()
                    } else {
                        // değerlendirme yok yazısını kaldır.
                        reviewAdapter.reviewList = reviewList
                        binding.textViewReviewContentCount.text = "Değerlendirme (${reviewList.size})"
                        binding.emptyReview.hide()
                    }
                    reviewAdapter.notifyChanges()
                }
                is Resource.Loading -> {
                    binding.progressBarProductDetail.show()
                }
                is Resource.Error -> {
                    binding.progressBarProductDetail.hide()
                    toast(requireContext(),it.message?:"Değerlendirmeler alınamadı.",false)
                }
            }
        }
    }

    private fun setProductView(product: Product) {
        binding.toolBarProductDetail.title = "Kategoriler/ ${product.productCategory}"
        binding.imageViewProductPhoto.glide(product.productPhoto, placeHolderProgressBar(binding.root.context))
        binding.textViewProductBrand.text = product.productBrand
        binding.textViewProductDetail.text = product.productDetail
        binding.textViewPrice.text = "${product.productPriceWhole}.${product.productPriceCent.toCent()} TL"
        binding.textViewRate.text = product.productRate.toString()
        binding.textViewReviewCount.text = "(${product.productReviewCount})"
        //binding.textViewShipping.text = product.productShipping
        //binding.textViewStore.text = product.productStore
        //binding.textViewStoreRate.text = product.productStoreRate
        if (product.productRate < 4.0) {
            binding.imageViewStar.setImageResource(R.drawable.star_half)
        } else {
            binding.imageViewStar.setImageResource(R.drawable.star)
        }

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

        binding.imageViewShare.setOnClickListener {
            val link = "https://www.eticaretuygulamasi.com/urun_detay?id=${product.productId}"
            shareContent("title", link)
        }

        binding.imageViewFavorite.setOnClickListener {
            if (userId != Constants.ANOMIM_USER_ID) {
                if (product.addedToFavorites) {
                    viewModel.removeFromFavorites(userId,product.productId)
                } else {
                    viewModel.addToFavorites(userId,product.productId)
                }
            } else {
                toast(requireContext(),"Favori işlemleri için giriş yapmalısınız.",false)
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        binding.buttonAddCart.setOnClickListener {
            if (product.addedToShoppingCart) {
                viewModel.removeFromShoppingCart(userId,product.productId)
            } else {
                viewModel.addToShoppingCart(userId,product.productId)
            }
        }
    }

    private fun shareContent(title: String, url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(intent, "Paylaş"))
    }

    private fun setRecyclerView() {
        binding.recyclerViewProductDetail.adapter = reviewAdapter
        binding.recyclerViewProductDetail.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewProductDetail.addItemDecoration(ItemDecoration(40,40,30))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}