package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen.views

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
import com.hasanalic.ecommerce.databinding.FragmentShoppingCartBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutActivity
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen.ShoppingCartState
import com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen.ShoppingCartViewModel
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShoppingCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]
        viewModel.checkUserAndGetShoppingCartItemList()

        setupListeners()

        setupRecyclerView()

        setupObservers()
    }

    private fun setupListeners() {
        binding.buttonCompleteOrder.setOnClickListener {
            viewModel.completeOrder()
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
            viewModel.increaseItemQuantityInShoppingCart(productId, quantity)
            //shoppingCartAdapter.notifyItemChangedInAdapter(position)
        }

        shoppingCartAdapter.setOnDecreaseButtonClickListener { productId, quantity, position ->
            viewModel.decreaseItemQuantityInShoppingCart(productId, quantity)
            //shoppingCartAdapter.notifyItemChangedInAdapter(position)
        }

        shoppingCartAdapter.setOnDeleteButtonClickListener { productId, position ->
            showDeleteItemWarnAlertDialog(productId, position)
        }
    }

    private fun showDeleteItemWarnAlertDialog(productId: String, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Are you sure you want to remove the item from your cart?")
        alertDialogBuilder.setPositiveButton("Remove") { _, _ ->
            viewModel.removeItemFromShoppingCart(productId)
            //shoppingCartAdapter.notifyItemRemovedInAdapter(position)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> }

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

        if (state.shouldUserMoveToAuthActivity) {
            toast(requireContext(), "You must log in to proceed with the purchase.", true)
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        if (state.canUserMoveToCheckout) {
            val intent = Intent(requireActivity(), CheckoutActivity::class.java)
            launcherForCheckout.launch(intent)
        }

        state.shoppingCartItemList.let {
            val shoppingCartItemList = it.toList()
            shoppingCartAdapter.shoppingCartItems = shoppingCartItemList
            shoppingCartAdapter.notifyDataSetChangedInAdapter()

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

    private val launcherForCheckout = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.checkUserAndGetShoppingCartItemList()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.checkUserAndGetShoppingCartItemList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}