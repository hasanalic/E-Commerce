package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen.views

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
import com.hasanalic.ecommerce.databinding.FragmentShoppingCartBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutActivity
import com.hasanalic.ecommerce.feature_home.presentation.HomeActivity
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen.ShoppingCartState
import com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen.ShoppingCartViewModel
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCartFragment: Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ShoppingCartViewModel

    private lateinit var sharedViewModel: SharedViewModel

    private val shoppingCartAdapter by lazy {
        ShoppingCartAdapter()
    }

    private var homeActivity: HomeActivity? = null

    private var userId: String = Constants.ANOMIM_USER_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onStart() {
        super.onStart()
        homeActivity?.hideToolBar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShoppingCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]
        viewModel.getShoppingCartItemList(userId)

        setupListeners()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupListeners() {
        binding.buttonCompleteOrder.setOnClickListener {
            if (viewModel.shoppingCartState.value!!.shoppingCartItemList.size != 0) {
                if (userId != Constants.ANOMIM_USER_ID) {
                    //viewModel.saveShoppinCartListToSingleton()
                    val intent = Intent(requireActivity(), CheckoutActivity::class.java)
                    launcherForCheckout.launch(intent)
                } else {
                    showCheckoutWarnAlertDialog()
                }
            } else {
                toast(requireContext(),"Sepetinizde ürün bulunmamaktadır",false)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewShoppingCart.adapter = shoppingCartAdapter
        binding.recyclerViewShoppingCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        shoppingCartAdapter.setOnCardClickListener { productId ->
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),productId)
            launcher.launch(intent)
        }

        shoppingCartAdapter.setOnIncreaseButtonClickListener { productId, quantity, position ->
            viewModel.increaseItemQuantityInShoppingCart(userId, productId, quantity, position)
            shoppingCartAdapter.notifyItemChangedInAdapter(position)
        }

        shoppingCartAdapter.setOnDecreaseButtonClickListener { productId, quantity, position ->
            viewModel.decreaseItemQuantityInShoppingCart(userId, productId, quantity, position)
            shoppingCartAdapter.notifyItemChangedInAdapter(position)
        }

        shoppingCartAdapter.setOnDeleteButtonClickListener { productId, position ->
            showDeleteItemWarnAlertDialog(userId, productId, position)
        }
    }

    private fun showDeleteItemWarnAlertDialog(userId: String, productId: String, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Ürünü sepetten silmek istediğine emin misin?")
        alertDialogBuilder.setPositiveButton("Sil") { _, _ ->
            viewModel.removeItemFromShoppingCart(userId, productId, position)
            shoppingCartAdapter.notifyItemRemovedInAdapter(position)
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ -> }

        alertDialogBuilder.create().show()
    }

    private fun setupObservers() {
        viewModel.shoppingCartState.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: ShoppingCartState) {
        if (state.isLoading) {
            binding.progressBarShoppingCart.show()
            binding.recyclerViewShoppingCart.hide()
            binding.buttonCompleteOrder.isEnabled = false
        } else {
            binding.progressBarShoppingCart.hide()
            binding.recyclerViewShoppingCart.show()
            binding.buttonCompleteOrder.isEnabled = true
        }

        state.shoppingCartItemList.let {
            val shoppingCartItemList = it.toList()
            shoppingCartAdapter.shoppingCartItems = shoppingCartItemList

            if (shoppingCartItemList.isEmpty()) {
                binding.emptyShoppingCart.show()
                binding.textViewCartSize.hide()
                binding.textViewTotal.hide()
                binding.textViewTotalTitle.hide()
                binding.buttonCompleteOrder.hide()
            } else {
                binding.emptyShoppingCart.hide()
                binding.textViewCartSize.show()
                binding.textViewCartSize.text = shoppingCartItemList.size.toString()
                binding.textViewTotalTitle.show()
                binding.textViewTotal.show()
                binding.buttonCompleteOrder.show()
            }
            shoppingCartAdapter.notifyDataSetChangedInAdapter()
        }

        if (state.totalPriceWhole != null && state.totalPriceCent != null) {
            val totalPrice = "${state.totalPriceWhole},${state.totalPriceCent} TL"
            binding.textViewTotal.text = totalPrice
        }

        state.dataError?.let {
            binding.recyclerViewShoppingCart.hide()
            TODO("add data error TextView")
        }

        state.actionError?.let {
            toast(requireContext(), it, true)
        }
    }

    /*
    private fun observer() {
        viewModel.stateShoppingCartItemSize.observe(viewLifecycleOwner) {
            sharedViewModel.updateCartItemCount(it)

            if (it != 0) {
                binding.textViewCartSize.text = "$it Ürün"
            } else {
                binding.textViewCartSize.text = ""
            }
        }
    }
     */

    private fun showCheckoutWarnAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Giriş Yapmadınız")
        alertDialogBuilder.setMessage("Satın alma işlemine geçmek istediğinize emin misin?")
        alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
            //viewModel.saveShoppinCartListToSingleton()
            val intent = Intent(requireActivity(), CheckoutActivity::class.java)
            launcherForCheckout.launch(intent)
        }
        alertDialogBuilder.setNegativeButton("Giriş Yap") { _, _ ->
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        alertDialogBuilder.create().show()
    }

    private val launcherForCheckout = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getShoppingCartItemList(userId)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getShoppingCartItemList(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}