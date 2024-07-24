package com.hasanalic.ecommerce.feature_shopping_cart.presentation

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
import com.hasanalic.ecommerce.feature_home.presentation.views.HomeActivity
import com.hasanalic.ecommerce.feature_auth.presentation.MainActivity
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.Resource
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

    //private lateinit var auth: FirebaseAuth
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

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            userId = it.uid
        }

         */

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]
        viewModel.getShoppingCartList(userId)

        binding.buttonCompleteOrder.setOnClickListener {
            if (viewModel.stateShoppingCartItemSize.value != 0) {
                if (userId != Constants.ANOMIM_USER_ID) {
                    viewModel.saveShoppinCartListToSingleton()
                    val intent = Intent(requireActivity(), CheckoutActivity::class.java)
                    launcherForCheckout.launch(intent)
                } else {
                    showCheckoutWarn()
                }
            } else {
                toast(requireContext(),"Sepetinizde ürün bulunmamaktadır",false)
            }
        }

        setRecyclerView()

        observer()
    }

    private fun observer() {
        viewModel.stateShoppingCartItems.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarShoppingCart.hide()
                    val shoppingCartList = it.data?.toList()

                    if (shoppingCartList.isNullOrEmpty()) {
                        shoppingCartAdapter.shoppingCartItems = listOf()
                        binding.emptyShoppingCart.show()
                    } else {
                        binding.emptyShoppingCart.hide()
                        shoppingCartAdapter.shoppingCartItems = shoppingCartList
                    }

                    shoppingCartAdapter.notifyChanges()
                }
                is Resource.Error -> {
                    binding.progressBarShoppingCart.hide()
                }
                is Resource.Loading -> {
                    binding.progressBarShoppingCart.show()
                }
            }
        }

        viewModel.stateTotal.observe(viewLifecycleOwner) {
            binding.textViewTotal.text = it
        }

        viewModel.stateShoppingCartItemSize.observe(viewLifecycleOwner) {
            sharedViewModel.updateCartItemCount(it)

            if (it != 0) {
                binding.textViewCartSize.text = "$it Ürün"
            } else {
                binding.textViewCartSize.text = ""
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerViewShoppingCart.adapter = shoppingCartAdapter
        binding.recyclerViewShoppingCart.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        shoppingCartAdapter.setOnDecreaseButtonClickListener {
            viewModel.decreaseShoppingCartItem(userId,it)
            shoppingCartAdapter.notifyChanges()
        }
        shoppingCartAdapter.setOnIncreaseButtonClickListener {
            viewModel.increaseShoppingCartItem(userId,it)
            shoppingCartAdapter.notifyChanges()
        }
        shoppingCartAdapter.setOnDeleteButtonClickListener {
            showDeleteWarn(userId,it)
        }
        shoppingCartAdapter.setOnCardClickListener {
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),it)
            launcher.launch(intent)
        }
    }

    private fun showDeleteWarn(userId: String, shoppingCartItemId: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Ürünü sepetten silmek istediğine emin misin?")
        alertDialogBuilder.setPositiveButton("Sil") { _, _ ->
            viewModel.deleteShoppingCartItem(userId, shoppingCartItemId)
            shoppingCartAdapter.notifyChanges()
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ ->
            shoppingCartAdapter.notifyChanges()
        }

        alertDialogBuilder.create().show()
    }

    private fun showCheckoutWarn() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Giriş Yapmadınız")
        alertDialogBuilder.setMessage("Satın alma işlemine geçmek istediğinize emin misin?")
        alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
            viewModel.saveShoppinCartListToSingleton()
            val intent = Intent(requireActivity(), CheckoutActivity::class.java)
            launcherForCheckout.launch(intent)
        }
        alertDialogBuilder.setNegativeButton("Giriş Yap") { _, _ ->
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        alertDialogBuilder.create().show()
    }

    private val launcherForCheckout = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getShoppingCartList(userId)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getShoppingCartList(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}