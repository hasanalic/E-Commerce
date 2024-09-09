package com.hasanalic.ecommerce.feature_home.presentation.favorite_screen.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentFavoriteBinding
import com.hasanalic.ecommerce.feature_home.presentation.favorite_screen.FavoriteState
import com.hasanalic.ecommerce.feature_home.presentation.favorite_screen.FavoriteViewModel
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment: Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private val favoriteAdapter by lazy {
        FavoriteAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        viewModel.getUserFavoriteProductsIfUserLoggedIn()
        //viewModel.getShoppingCartCount(userId)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFavorite.adapter = favoriteAdapter
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        favoriteAdapter.setOnRemoveFromFavoriteClickListener { productId ->
            showRemoveFromFavoriteWarningDialog(productId)
        }

        favoriteAdapter.setOnAddCartButtonClickListener { productId ->
            viewModel.addProductToCart(productId)
            //favoriteAdapter.notifyItemChangedInAdapter(position)
        }

        favoriteAdapter.setOnRemoveFromCartButtonClickListener { productId ->
            viewModel.removeProductFromCart(productId)
        }

        favoriteAdapter.setOnCardClickListener { productId ->
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id), productId)
            launcher.launch(intent)
        }
    }

    private fun showRemoveFromFavoriteWarningDialog(productId: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Are you sure you want to remove this item from your favorites?")
        alertDialogBuilder.setPositiveButton("Remove") { _, _ ->
            viewModel.removeProductFromFavorites(productId)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> }

        alertDialogBuilder.create().show()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getUserFavoriteProductsIfUserLoggedIn()
        //viewModel.getShoppingCartCount(userId)
    }

    private fun setupObservers() {
        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: FavoriteState) {
        if (state.isLoading) {
            binding.progressBarFavorite.show()
            binding.recyclerViewFavorite.hide()
        } else {
            binding.progressBarFavorite.hide()
            binding.recyclerViewFavorite.show()
        }

        if (!state.isUserLoggedIn) {
            navigateToAuthActivityAndFinish()
        }

        state.favoriteProductList.let {
            val favoriteProductList = it.toList()
            favoriteAdapter.favoriteProducts = favoriteProductList
            favoriteAdapter.notifyDataSetChangedInAdapter()

            if (favoriteProductList.isEmpty()) {
                binding.emptyFavorite.show()
            } else {
                binding.emptyFavorite.hide()

            }
        }

        state.dataError?.let {
            binding.recyclerViewFavorite.hide()
            TODO("add data error TextView")
        }

        state.actionError?.let {
            toast(requireContext(), it, false)
        }
    }

    private fun navigateToAuthActivityAndFinish() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        startActivity(intent)
        toast(requireContext(),"Log in to your account to view your favorites.",false)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}