package com.hasanalic.ecommerce.feature_home.presentation.filtered_screen.views

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.databinding.FragmentFilteredProductsBinding
import com.hasanalic.ecommerce.feature_home.presentation.filtered_screen.FilteredProductsViewModel
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.views.HomeAdapter
import com.hasanalic.ecommerce.feature_home.presentation.util.SearchQuery
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilteredProductsFragment: Fragment() {

    private var _binding: FragmentFilteredProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FilteredProductsViewModel

    private var searchQuery: String? = null

    private val adapter by lazy {
        HomeAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Navigation.findNavController(binding.root).popBackStack()
                //viewModel.resetFilteredProductsStatus()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilteredProductsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FilteredProductsViewModel::class.java]

        SearchQuery.searchQuery?.let {
            binding.editTextSearch.setText(it)
            SearchQuery.searchQuery = null
        }

        /*
        gelen bir veri varsa (sesli aramanın texti vs) direkt arguments'den çekilir ve ilgili metod çağırılır.
         */

        setupListeners()

        setupRecyclerView()

        setupObservers()
    }

    private fun setupListeners() {
        binding.editTextSearch.requestFocus()
        binding.editTextSearch.postDelayed({
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.editTextSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchQuery = binding.editTextSearch.text.toString()
                binding.result.text = "\"$searchQuery\" için sonuçlar"
                //viewModel.getFilteredProductsBySearchQuery(userId,searchQuery!!)

                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.textInputLayoutSearch.setStartIconOnClickListener {
            Navigation.findNavController(it).popBackStack()
            //viewModel.resetFilteredProductsStatus()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFiltered.adapter = adapter
        binding.recyclerViewFiltered.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.recyclerViewFiltered.addItemDecoration(ItemDecoration(40,40,30))

        adapter.setAddProductToCartButtonClickListener { productId, position ->
            /*
            viewModel.addProductToCart(productId, position)
            homeAdapter.notifyItemChangedInAdapter(position)
             */
        }

        adapter.setRemoveProductFromCartButtonClickListener { productId, position ->
            /*
            viewModel.removeProductFromCart(productId, position)
            homeAdapter.notifyItemChangedInAdapter(position)
             */
        }

        adapter.setAddProductToFavoritesClickListener { productId, position ->
            //viewModel.addProductToFavoritesIfUserAuthenticated(productId, position)
        }

        adapter.setRemoveProductFromFavoritesClickListener { productId, position ->
            //viewModel.removeProductFromFavoritesIfUserAuthenticated(productId, position)
        }

        adapter.setOnProductClickListener { productId ->
            /*
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),productId)
            launcher.launch(intent)
             */
        }
    }

    private fun setupObservers() {

    }

    private fun observe() {
        /*
        viewModel.stateFilteredProducts.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarFiltered.hide()
                    val productList = it.data

                    if (productList.isNullOrEmpty()) {
                        adapter.products = listOf()
                        binding.founded.text = "0 tane bulundu"
                    } else {
                        adapter.products = productList.toList()
                        binding.founded.text = "${productList.size} tane bulundu"
                    }

                    adapter.notifyChanges()
                }
                is Resource.Error -> {
                    binding.progressBarFiltered.hide()
                    toast(requireContext(),it.message!!,false)
                }
                is Resource.Loading -> {
                    binding.progressBarFiltered.show()
                }
            }
        }

        viewModel.stateShoppingCartItemSize.observe(viewLifecycleOwner) {
            sharedViewModel.updateCartItemCount(it)
        }

        viewModel.stateCompareCounter.observe(viewLifecycleOwner) {
            if (it != 0) {
                binding.materialCardCompare.show()
            } else {
                binding.materialCardCompare.hide()
            }
        }

         */
    }

    private fun setRecyclerView() {
        /*
        binding.recyclerViewFiltered.adapter = adapter
        binding.recyclerViewFiltered.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        adapter.setAddCartButtonClickListener {
            viewModel.addShoppingCartFilteredProducts(userId,it)
            adapter.notifyChanges()
        }
        adapter.setRemoveCartButtonClickListener {
            viewModel.removeFromShoppingCartFilteredProducts(userId,it)
            adapter.notifyChanges()
        }
        adapter.setOnCompareClickListener {
            viewModel.clickOnCompareFilteredList(it)
            adapter.notifyChanges()
        }
        adapter.setAddFavoriteClickListener {
            if (userId != Constants.ANOMIM_USER_ID) {
                viewModel.addFavoriteFilteredProducts(userId,it)
                adapter.notifyChanges()
            } else {
                toast(requireContext(),"Favori işlemleri için giriş yapmalısınız.",false)
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        adapter.removeFavoriteClickListener {
            if (userId != Constants.ANOMIM_USER_ID) {
                viewModel.removeFromFavoriteFilteredProducts(userId,it)
                adapter.notifyChanges()
            } else {
                toast(requireContext(),"Favori işlemleri için giriş yapmalısınız.",false)
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        adapter.setOnProductClickListener {
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),it)
            launcherToProductDetail.launch(intent)
        }

         */
    }

    private val launcherToProductDetail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //viewModel.getShoppingCartCount(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}