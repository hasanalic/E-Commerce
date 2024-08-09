package com.hasanalic.ecommerce.feature_home.presentation.home_screen.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentHomeBinding
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_filter.presentation.FilterActivity
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.feature_home.presentation.barcode_screen.BarcodeScannerActivity
import com.hasanalic.ecommerce.feature_home.presentation.filtered_category_screen.CategoryAdapter
import com.hasanalic.ecommerce.feature_home.presentation.HomeActivity
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.HomeState
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.HomeViewModel
import com.hasanalic.ecommerce.feature_home.presentation.util.SearchQuery
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.utils.Constants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.utils.ItemDecoration
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var homeActivity: HomeActivity? = null

    private var isFabMenuOpen: Boolean = false

    private var userId: String = ANOMIM_USER_ID

    private val homeAdapter by lazy {
        HomeAdapter()
    }

    private val categoryAdapter by lazy {
        CategoryAdapter()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onStart() {
        super.onStart()
        homeActivity?.showToolBar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {result ->
            if (result) {
                val intent = Intent(requireActivity(), BarcodeScannerActivity::class.java)
                launcherBarcodeScanner.launch(intent)
            } else {
                toast(requireContext(),requireActivity().getString(R.string.permission_needed),false)
            }
        }

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        viewModel.getProducts(userId)
        //viewModel.getShoppingCartCount(userId)
        //viewModel.resetProductIdStatus()


        setupFloatingActionButtons()

        setupListeners()

        setupRecyclerViewCategory()

        setupRecyclerViewHome()

        setupObservers()
    }

    private fun setupFloatingActionButtons() {
        binding.extendedFabSettings.hide()
        binding.floatingActionButtonBarcode.hide()
        binding.floatingActionButtonVoice.hide()

        binding.floatingActionButtonMainAdd.setOnClickListener {
            toggleFabMenu()
        }
        binding.extendedFabSettings.setOnClickListener {
            toggleFabMenu()
        }
        binding.floatingActionButtonVoice.setOnClickListener {
            speachToTextAndSearch()
        }
        binding.floatingActionButtonBarcode.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
                    Snackbar.make(it, requireActivity().getString(R.string.permission_need), Snackbar.LENGTH_LONG).setAction("İzin ver") {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }.show()
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            } else {
                val intent = Intent(requireActivity(), BarcodeScannerActivity::class.java)
                launcherBarcodeScanner.launch(intent)
            }
        }
    }

    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            binding.extendedFabSettings.hide()
            binding.floatingActionButtonVoice.hide()
            binding.floatingActionButtonBarcode.hide()
            binding.floatingActionButtonMainAdd.show()
        } else {
            binding.floatingActionButtonMainAdd.hide()
            binding.extendedFabSettings.show()
            binding.floatingActionButtonVoice.show()
            binding.floatingActionButtonBarcode.show()
        }
        isFabMenuOpen = !isFabMenuOpen
    }

    private fun setupListeners() {
        binding.materialViewFilter.setOnClickListener {
            val intent = Intent(requireActivity(), FilterActivity::class.java)
            launcherFilterActivity.launch(intent)
        }
        binding.editTextSearch.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_filteredProductsFragment)
        }
        binding.materialCardCompare.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_compareFragment)
        }
    }

    private fun setupRecyclerViewCategory() {
        binding.recyclerViewCategory.adapter = categoryAdapter
        binding.recyclerViewCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewCategory.addItemDecoration(ItemDecoration(0,16,0))

        categoryAdapter.setOnChipClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFilteredProductsByCategoryFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerViewHome() {
        binding.recyclerViewHome.adapter = homeAdapter
        binding.recyclerViewHome.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        homeAdapter.setAddProductToCartButtonClickListener { productId, position ->
            viewModel.addProductToCart(userId, productId, position)
            homeAdapter.notifyItemChangedInAdapter(position)
        }

        homeAdapter.setRemoveProductFromCartButtonClickListener { productId, position ->
            viewModel.removeProductFromCart(userId, productId, position)
            homeAdapter.notifyItemChangedInAdapter(position)
        }

        homeAdapter.setAddProductToFavoritesClickListener { productId, position ->
            if (userId != ANOMIM_USER_ID) {
                viewModel.addProductToFavorites(userId, productId, position)
                homeAdapter.notifyItemChangedInAdapter(position)
            } else {
                toast(requireContext(),"Favori işlemleri için giriş yapmalısınız.",false)
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        homeAdapter.setRemoveProductFromFavoritesClickListener { productId, position ->
            if (userId != ANOMIM_USER_ID) {
                viewModel.removeProductFromFavorites(userId, productId, position)
                homeAdapter.notifyItemChangedInAdapter(position)
            } else {
                toast(requireContext(),"Favori işlemleri için giriş yapmalısınız.",false)
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        homeAdapter.setAddProductToCompareClickListener { productId, position ->
            TODO()
        }

        homeAdapter.setRemoveProductFromCompareClickListener { productId, position ->
            TODO()
        }

        homeAdapter.setOnProductClickListener { productId ->
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),productId)
            launcher.launch(intent)
        }
    }

    private fun setupObservers() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            handleHomeState(state)
        }
    }

    private fun handleHomeState(state: HomeState) {
        if (state.isLoading) {
            binding.progressBarHome.show()
            binding.recyclerViewHome.hide()
            binding.recyclerViewCategory.hide()
        } else {
            binding.progressBarHome.hide()
            binding.recyclerViewHome.show()
            binding.recyclerViewCategory.show()
        }

        state.productList.let {
            homeAdapter.products = it.toList()
            homeAdapter.notifyDataSetChangedInAdapter()
        }

        state.categoryList.let {
            categoryAdapter.categoryList = it.toList()
            categoryAdapter.notifyChanges()
        }

        state.dataError?.let {
            binding.recyclerViewHome.hide()
            binding.recyclerViewCategory.hide()
            TODO("DATA ERROR TEXTVIEW")
        }

        state.actionError?.let {
            toast(requireContext(), it, false)
        }
    }

    private fun speachToTextAndSearch() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,requireActivity().getString(R.string.speech))

        try {
            launcherSpeechToText.launch(intent)
        } catch (e: Exception) {
            toast(requireContext()," " + e.message, true)
        }
    }

    private val launcherBarcodeScanner =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val barcode = data.getStringExtra(getString(R.string.barcode))
                barcode?.let {
                    //viewModel.getProductIdByBarcode(barcode)
                }
            }
        } else {
            val data: Intent? = result.data
            data?.let {
                val error = data.getStringExtra(getString(R.string.barcode))
                toast(requireContext(),error?: "hata",false)
            }
        }
    }

    private val launcherSpeechToText = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val res: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                SearchQuery.searchQuery = res.toList().joinToString()
                //viewModel.getFilteredProductsBySearchQuery(userId,res.toList().joinToString())
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_filteredProductsFragment)
            }
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getProducts(userId)
        //viewModel.getShoppingCartCount(userId)
    }

    private val launcherFilterActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val receivedData = data.getBooleanExtra(requireActivity().getString(R.string.should_move_to_filtered_fragment), false)
                if (receivedData) {
                    //viewModel.getFilteredProductsByFilter(userId, FilterSingleton.filter!!)
                    Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_filteredProductsFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}