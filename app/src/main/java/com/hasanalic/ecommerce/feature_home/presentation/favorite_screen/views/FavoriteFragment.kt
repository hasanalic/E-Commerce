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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment: Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var userId: String

    private val favoriteAdapter by lazy {
        FavoriteAdapter()
    }

    /*
    override fun onStart() {
        super.onStart()
        homeActivity?.hideToolBar()

        /*
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            toast(requireContext(),"Favorileri görüntülemek için hesabınıza giriş yapınız.",false)
            requireActivity().finish()
        }

         */
    }
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        //viewModel.getShoppingCartCount(userId)
        //viewModel.getUserFavoriteProducts(userId)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFavorite.adapter = favoriteAdapter
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        favoriteAdapter.setOnRemoveFromFavoriteClickListener { productId, position ->
            showRemoveFromFavoriteWarningDialog(userId, productId, position)
            favoriteAdapter.notifyItemRemovedInAdapter(position)
        }

        favoriteAdapter.setOnAddCartButtonClickListener { productId, position ->
            viewModel.addProductToCart(userId, productId, position)
            favoriteAdapter.notifyItemChangedInAdapter(position)
        }

        favoriteAdapter.setOnRemoveFromCartButtonClickListener { productId, position ->
            viewModel.removeProductFromCart(userId, productId, position)
        }

        favoriteAdapter.setOnCardClickListener { productId ->
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id), productId)
            launcher.launch(intent)
        }
    }

    private fun showRemoveFromFavoriteWarningDialog(userId: String, productId: String, itemIndex: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Ürünü favorilerden kaldırmak istediğine emin misin?")
        alertDialogBuilder.setPositiveButton("Kaldır") { _, _ ->
            viewModel.removeProductFromFavorites(userId, productId, itemIndex)
            favoriteAdapter.notifyItemRemovedInAdapter(itemIndex)
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ -> }

        alertDialogBuilder.create().show()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getUserFavoriteProducts(userId)
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

        state.favoriteProductList.let {
            val favoriteProductList = it.toList()

            if (favoriteProductList.isEmpty()) {
                binding.emptyFavorite.show()
            } else {
                binding.emptyFavorite.hide()
                favoriteAdapter.favoriteProducts = it.toList()
            }
            favoriteAdapter.notifyDataSetChangedInAdapter()
        }

        state.dataError?.let {
            binding.recyclerViewFavorite.hide()
            TODO("add data error TextView")
        }

        state.actionError?.let {
            toast(requireContext(), it, false)
        }
    }

    /*
    private fun observe() {
        viewModel.stateFavorites.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarFavorite.hide()
                    val favoriteList = it.data?.toList()

                    if (favoriteList.isNullOrEmpty()) {
                        favoriteAdapter.products = listOf()
                        binding.emptyFavorite.show()
                    } else {
                        binding.emptyFavorite.hide()
                        favoriteAdapter.products = favoriteList
                    }

                    favoriteAdapter.notifyChanges()
                }
                is Resource.Error -> {
                    binding.progressBarFavorite.hide()
                }
                is Resource.Loading -> {
                    binding.progressBarFavorite.show()
                }
            }
        }

        viewModel.stateShoppingCartItemSize.observe(viewLifecycleOwner) {
            sharedViewModel.updateCartItemCount(it)
        }
    }
     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}