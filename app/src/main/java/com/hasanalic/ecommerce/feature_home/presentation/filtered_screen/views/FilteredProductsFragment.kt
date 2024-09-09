package com.hasanalic.ecommerce.feature_home.presentation.filtered_screen.views

import android.content.Context
import android.content.Intent
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
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
import com.hasanalic.ecommerce.databinding.FragmentFilteredProductsBinding
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_filter.presentation.util.FilterSingleton
import com.hasanalic.ecommerce.feature_home.presentation.filtered_screen.FilteredProductsState
import com.hasanalic.ecommerce.feature_home.presentation.filtered_screen.FilteredProductsViewModel
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.views.HomeAdapter
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilteredProductsFragment: Fragment() {

    private var _binding: FragmentFilteredProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FilteredProductsViewModel

    private val adapter by lazy {
        HomeAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Navigation.findNavController(binding.root).popBackStack()
                viewModel.resetFilteredProductState()
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
        viewModel.checkUserId()

        showKeyword()

        setupListeners()

        handleProductFiltering(view)

        setupRecyclerView()

        setupObservers()
    }

    private fun handleProductFiltering(v: View) {
        arguments?.let {
            val keyword = it.getString("keyword")
            keyword?.let {
                binding.textInputLayoutSearch.visibility = View.VISIBLE
                binding.toolBarCategory.hide()
                showKeyword()

                viewModel.getProductsByKeyword(keyword)
                binding.editTextSearch.setText(keyword)
                binding.result.text = "Results for \"$keyword\""
            }

            val category = it.getString("category")
            category?.let {
                binding.textInputLayoutSearch.visibility = View.INVISIBLE
                binding.toolBarCategory.show()
                binding.toolBarCategory.title = "Categories/$category"
                hideKeyboard(v)

                viewModel.getProductsByCategory(category)
            }
        }

        FilterSingleton.filter?.let {
            viewModel.getProductsByFilter(it)
            FilterSingleton.filter = null
        }
    }

    private fun setupListeners() {
        binding.editTextSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val keyword = binding.editTextSearch.text.toString()
                binding.result.text = "Results for \"$keyword\""

                if (!keyword.isNullOrEmpty()) {
                    viewModel.getProductsByKeyword(keyword!!)
                }

                hideKeyboard(v)

                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.textInputLayoutSearch.setStartIconOnClickListener {
            Navigation.findNavController(it).popBackStack()
            viewModel.resetFilteredProductState()
        }

        binding.toolBarCategory.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
            viewModel.resetFilteredProductState()
        }
    }

    private fun hideKeyboard(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun showKeyword() {
        binding.editTextSearch.requestFocus()
        binding.editTextSearch.postDelayed({
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFiltered.adapter = adapter
        binding.recyclerViewFiltered.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.recyclerViewFiltered.addItemDecoration(ItemDecoration(40,40,30))

        adapter.setAddProductToCartButtonClickListener { productId, position ->
            viewModel.checkIfProductAlreadyInCart(productId, position)
        }

        adapter.setRemoveProductFromCartButtonClickListener { productId, position ->
            viewModel.checkIfProductNotInCart(productId, position)
        }

        adapter.setAddProductToFavoritesClickListener { productId, position ->
            viewModel.addProductToFavoritesIfUserAuthenticated(productId, position)
        }

        adapter.setRemoveProductFromFavoritesClickListener { productId, position ->
            viewModel.removeProductFromFavoritesIfUserAuthenticated(productId, position)
        }

        adapter.setOnProductClickListener { productId ->
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),productId)
            launcherToProductDetail.launch(intent)
        }
    }

    private fun setupObservers() {
        viewModel.filteredProducsState.observe(viewLifecycleOwner) {
            handleFilteredProductsState(it)
        }
    }

    private fun handleFilteredProductsState(state: FilteredProductsState) {
        if (state.isLoading) {
            binding.progressBarFiltered.show()
            binding.editTextSearch.isEnabled = false
        } else {
            binding.progressBarFiltered.hide()
            binding.editTextSearch.isEnabled = true
        }

        state.productList.let {
            adapter.products = it
            adapter.notifyDataSetChangedInAdapter()

            if (it.isEmpty()) {
                binding.founded.text = "0 items found"
                //TODO()
            } else {
                binding.founded.text = "${it.size} items found"
            }

            hideKeyboard(binding.root)
        }

        if (state.shouldUserMoveToAuthActivity) {
            navigateToAuthActivity()
        }

        state.actionError?.let {
            toast(requireContext(), it, false)
        }

        state.dataError?.let {
            TODO()
        }
    }

    private fun navigateToAuthActivity() {
        toast(requireContext(),"You must log in to use your favorite operations.",false)
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private val launcherToProductDetail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //viewModel.getShoppingCartCount(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}