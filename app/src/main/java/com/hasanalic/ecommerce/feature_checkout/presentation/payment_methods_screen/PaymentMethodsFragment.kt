package com.hasanalic.ecommerce.feature_checkout.presentation.payment_methods_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentPaymentMethodsBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast

class PaymentMethodsFragment: Fragment() {

    private var _binding: FragmentPaymentMethodsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CheckoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPaymentMethodsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBarPaymentMethods.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]

        binding.materialCardBankOrCreditCard.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_paymentMethodsFragment_to_cardFragment)
        }
        binding.materialCardDoor.setOnClickListener {
            viewModel.setOrderTypeAsDoorAndInitialize()
        }

        observe()
    }

    private fun observe() {
        viewModel.statusPayment.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarPaymentMethods.hide()
                    Navigation.findNavController(binding.root).navigate(R.id.action_paymentMethodsFragment_to_successFragment)
                }
                is Resource.Error -> {
                    binding.progressBarPaymentMethods.hide()
                    toast(requireContext(),it.message?:"hata",false)
                }
                is Resource.Loading -> {
                    binding.progressBarPaymentMethods.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}