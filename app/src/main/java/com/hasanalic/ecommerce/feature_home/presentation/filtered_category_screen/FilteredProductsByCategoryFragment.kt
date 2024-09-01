package com.hasanalic.ecommerce.feature_home.presentation.filtered_category_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.databinding.FragmentFilteredProductsByCategoryBinding
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.HomeViewModel
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.views.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilteredProductsByCategoryFragment: Fragment() {

    private var _binding: FragmentFilteredProductsByCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private lateinit var sharedViewModel: SharedViewModel

    private var userId: String = ANOMIM_USER_ID

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
        _binding = FragmentFilteredProductsByCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            userId = it.uid
        }

         */

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        //viewModel.getShoppingCartCount(userId)

        arguments?.getString("category")?.let {
            //viewModel.getFilteredProductsByCategory(userId,it)
            binding.toolBarFilteredProductsByCategory.title = "Kategoriler/$it"
        }

        binding.toolBarFilteredProductsByCategory.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
            //viewModel.resetFilteredProductsStatus()
        }

        binding.materialCardCompare.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_filteredProductsByCategoryFragment_to_compareFragment)
        }

        setRecyclerView()

        observe()
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