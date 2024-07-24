package com.hasanalic.ecommerce.feature_favorite.presentation

import android.app.AlertDialog
import android.content.Context
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
import com.hasanalic.ecommerce.feature_home.presentation.views.HomeActivity
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment: Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var sharedViewModel: SharedViewModel

    /*
    private lateinit var auth: FirebaseAuth

     */
    private lateinit var userId: String

    private val favoriteAdapter by lazy {
        FavoriteAdapter()
    }

    private var homeActivity: HomeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        auth = FirebaseAuth.getInstance()
        userId = auth.uid ?: ANOMIM_USER_ID

         */

        viewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        viewModel.getShoppingCartCount(userId)
        viewModel.getFavorites(userId)

        setRecyclerView()

        observe()
    }

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

    private fun setRecyclerView() {
        binding.recyclerViewFavorite.adapter = favoriteAdapter
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        favoriteAdapter.setOnFavoriteClickListener {
            showUnFavoriteWarning(userId,it)
            favoriteAdapter.notifyChanges()
        }
        favoriteAdapter.setOnCartButtonClickListener {
            viewModel.changeAddToShoppingCart(userId,it)
            favoriteAdapter.notifyChanges()
        }
        favoriteAdapter.setOnCardClickListener {
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),it)
            launcher.launch(intent)
        }
    }

    private fun showUnFavoriteWarning(userId: String, shoppingCartItemId: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Ürünü favorilerden kaldırmak istediğine emin misin?")
        alertDialogBuilder.setPositiveButton("Kaldır") { _, _ ->
            viewModel.unFavorite(userId,shoppingCartItemId)
            favoriteAdapter.notifyChanges()
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ ->
            favoriteAdapter.notifyChanges()
        }

        alertDialogBuilder.create().show()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getFavorites(userId)
        viewModel.getShoppingCartCount(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}