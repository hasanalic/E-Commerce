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
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutState
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast

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

        viewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]

        setupListeners()

        setupObservers()
    }

    private fun setupListeners() {
        binding.toolBarPaymentMethods.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.materialCardBankOrCreditCard.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_paymentMethodsFragment_to_cardFragment)
        }

        binding.materialCardDoor.setOnClickListener {
            viewModel.buyOrderAtDoor()
        }
    }

    private fun setupObservers() {
        viewModel.checkoutState.observe(viewLifecycleOwner) {
            handleCheckoutState(it)
        }
    }

    private fun handleCheckoutState(state: CheckoutState) {
        if (state.isLoading) {
            binding.progressBarPaymentMethods.show()
        } else {
            binding.progressBarPaymentMethods.hide()
        }

        if (state.isPaymentSuccessful) {
            Navigation.findNavController(binding.root).navigate(R.id.action_paymentMethodsFragment_to_successFragment)
        }

        state.dataError?.let {
            toast(requireContext(), it, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}