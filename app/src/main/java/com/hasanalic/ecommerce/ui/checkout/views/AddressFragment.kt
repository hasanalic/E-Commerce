package com.hasanalic.ecommerce.ui.checkout.views

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
import com.hasanalic.ecommerce.ui.location.LocationActivity
import com.hasanalic.ecommerce.ui.checkout.CheckoutViewModel
import com.hasanalic.ecommerce.utils.Constants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.utils.ItemDecoration
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment: Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CheckoutViewModel

    //private lateinit var auth: FirebaseAuth
    private var userId: String = ANOMIM_USER_ID

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

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            userId = it.uid
        }

         */

        viewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]
        viewModel.getAddressList(userId)

        binding.toolBarAddress.setNavigationOnClickListener {
            requireActivity().finish()
        }

        binding.buttonContinue.setOnClickListener {view->
            addressId?.let {
                viewModel.setOrderAddressAndUserId(it, userId)
                Navigation.findNavController(view).navigate(R.id.action_addressFragment_to_shippingFragment)
            }?: toast(requireContext(),"Adres seçiniz",false)
        }

        binding.buttonAddNewAddress.setOnClickListener {
            val intent = Intent(requireActivity(), LocationActivity::class.java)
            launcher.launch(intent)
        }

        setRecyclerView()

        observe()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.getAddressList(userId)
    }

    private fun observe() {
        viewModel.statusAddressList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarAddress.hide()
                    addressAdapter.addressList = it.data ?: listOf()
                    addressAdapter.notifyChanges()
                }
                is Resource.Loading -> {
                    binding.progressBarAddress.show()
                }
                is Resource.Error -> {
                    binding.progressBarAddress.hide()
                    toast(requireContext(),it.message?:"Hata",false)
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerViewAddress.adapter = addressAdapter
        binding.recyclerViewAddress.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewAddress.addItemDecoration(ItemDecoration(40,40,40))

        addressAdapter.setOnCardClickListener {
            viewModel.setAddress(it)
            addressAdapter.notifyChanges()
            addressId = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addressId = null
        _binding = null
    }
}