package com.hasanalic.ecommerce.feature_checkout.presentation.address_screen.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentAddressBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel
import com.hasanalic.ecommerce.feature_location.presentation.LocationActivity
import com.hasanalic.ecommerce.feature_checkout.presentation.address_screen.AddressState
import com.hasanalic.ecommerce.feature_checkout.presentation.address_screen.AddressViewModel
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment: Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddressViewModel
    private lateinit var checkoutViewModel: CheckoutViewModel

    private var addressId: String? = null

    private val addressAdapter by lazy {
        AddressAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AddressViewModel::class.java]
        checkoutViewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]
        viewModel.getAddressList()

        setupListeners()

        setupRecyclerView()

        setupObservers()
    }

    private fun setupListeners() {
        binding.toolBarAddress.setNavigationOnClickListener {
            requireActivity().finish()
        }

        binding.buttonContinue.setOnClickListener {view->
            addressId?.let {
                checkoutViewModel.getUserIdAndSetOrderAddressId(it)
                Navigation.findNavController(view).navigate(R.id.action_addressFragment_to_shippingFragment)
            }?: toast(requireContext(),"Select an address",false)
        }

        binding.buttonAddNewAddress.setOnClickListener {
            val intent = Intent(requireActivity(), LocationActivity::class.java)
            launcher.launch(intent)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getAddressList()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewAddress.adapter = addressAdapter
        binding.recyclerViewAddress.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewAddress.addItemDecoration(ItemDecoration(40,40,40))

        addressAdapter.setOnCardClickListener { position, clickedAddressId ->
            viewModel.selectAddress(position)
            addressAdapter.notifyChanges()
            addressId = clickedAddressId
        }
    }

    private fun setupObservers() {
        viewModel.addressState.observe(viewLifecycleOwner) {
            handleAddressState(it)
        }
    }

    private fun handleAddressState(state: AddressState) {
        if (state.isLoading) {
            binding.progressBarAddress.show()
        } else {
            binding.progressBarAddress.hide()
        }

        state.addressList.let {
            addressAdapter.addressList = it
            addressAdapter.notifyChanges()
        }

        state.dataError?.let {
            TODO()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addressId = null
        _binding = null
    }
}