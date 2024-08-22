package com.hasanalic.ecommerce.feature_checkout.presentation.shipping_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentShippingBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel

class ShippingFragment: Fragment() {

    private var _binding: FragmentShippingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CheckoutViewModel

    private var cargoName = "MNG Kargo"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShippingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]

        binding.toolBarShipping.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.buttonContinue.setOnClickListener {
            viewModel.setOrderCargo(cargoName)
            Navigation.findNavController(it).navigate(R.id.action_shippingFragment_to_paymentMethodsFragment)
        }

        binding.mngKargoRadioButton.setOnClickListener {
            cargoName = "MNG Kargo"
        }
        binding.arasKargoRadioButton.setOnClickListener {
            cargoName = "Aras Kargo"
        }
        binding.yurticiKargoRadioButton.setOnClickListener {
            cargoName = "Yurtiçi Kargo"
        }
        binding.pttKargoRadioButton.setOnClickListener {
            cargoName = "PTT Kargo"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}