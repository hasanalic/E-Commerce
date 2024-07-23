package com.hasanalic.ecommerce.ui.favorite

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentFavoriteBinding
import com.hasanalic.ecommerce.domain.model.Product
import com.hasanalic.ecommerce.ui.HomeActivity
import com.hasanalic.ecommerce.ui.MainActivity
import com.hasanalic.ecommerce.ui.ProductDetailActivity
import com.hasanalic.ecommerce.ui.home.SharedViewModel
import com.hasanalic.ecommerce.utils.Constants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
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

        val mainHandler = Handler()
        mainHandler.postDelayed({
            if (favoriteAdapter.products.isNotEmpty()) {
                showDiscountPopup(favoriteAdapter.products)
            }
        }, 1700)

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

    private fun showDiscountPopup(favoriteList: List<Product>) {
        val randomProduct = favoriteList.random()
        val randomTitle = listOf("İndirimi Kaçırma!","Fırsat Zamanı!","İndirim Sürüyor!","Kaçırma Fırsatı!","Yeni İndirim!","Son İndirim!").random()
        val randomDiscount = (1..5).random() * 10
        val randomDetail = getDiscountPopupDetail(randomProduct.productDetail, randomDiscount.toString())

        if (_binding != null) {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_discount,null)
            val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()
            dialogView.setBackgroundColor(requireActivity().getColor(R.color.white))

            val buttonOk = dialogView.findViewById<Button>(R.id.buttonOk)
            val textViewTitle = dialogView.findViewById<android.widget.TextView>(R.id.textViewTitle)
            val textViewDetail = dialogView.findViewById<android.widget.TextView>(R.id.textViewDetail)

            textViewTitle.text = randomTitle
            textViewDetail.text = randomDetail

            buttonOk.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun getDiscountPopupDetail(product: String, discount: String): String {
        val randomDetails = listOf(
            "$product'i %$discount indirimle hemen alabilirsin! Son şansı kaçırma, sipariş ver.",
            "$product'i %$discount indirimle hemen alabilirsin! Hızlıca karar ver, fırsatı kaçırma.",
            "$product'i %$discount indirimle almak için son gün! Acele et, avantajları kaçırma.",
            "%$discount indirimle $product'i almak için son şans! Hemen sipariş ver, avantajları kaçırma.",
            "$product'i %$discount indirimle alabilirsin! Acele et, stoklarla sınırlı.",
            "$product için %$discount indirim fırsatını kaçırma! Hemen satın al ve avantajları yakala.")
            .random()
        return randomDetails
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