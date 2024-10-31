package com.hasanalic.ecommerce.feature_auth.presentation.login.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.feature_auth.presentation.login.LoginState
import com.hasanalic.ecommerce.feature_auth.presentation.login.LoginViewModel
import com.hasanalic.ecommerce.feature_auth.presentation.register.views.RegisterFragment
import com.hasanalic.ecommerce.feature_home.presentation.HomeActivity
import com.hasanalic.ecommerce.core.utils.toast
import com.hasanalic.ecommerce.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
//        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.buttonGuest.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.textViewRegister.setOnClickListener {
            navigateToRegisterFragment()
        }
    }

    private fun setupObservers() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: LoginState) {
        if (state.isLoginSuccessful) {
            navigateToHomeActivity()
        }

        state.validationError?.let {
            toast(requireContext(), it, false)
            viewModel.clearValidationError()
        }

        state.dataError?.let {
            toast(requireContext(), it, false)
            viewModel.clearDataError()
        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToRegisterFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.fragmentContainerViewMainActivity, RegisterFragment())
            commit()
        }
    }
}