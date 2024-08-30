package com.hasanalic.ecommerce.feature_auth.presentation.register.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.core.presentation.BaseFragment
import com.hasanalic.ecommerce.databinding.FragmentRegisterBinding
import com.hasanalic.ecommerce.feature_auth.presentation.login.views.LoginFragment
import com.hasanalic.ecommerce.feature_auth.presentation.register.RegisterState
import com.hasanalic.ecommerce.feature_auth.presentation.register.RegisterViewModel
import com.hasanalic.ecommerce.feature_home.presentation.HomeActivity
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast

class RegisterFragment: BaseFragment<FragmentRegisterBinding>(
    bindingInflater = FragmentRegisterBinding::inflate
) {

    private lateinit var viewModel: RegisterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.buttonGuest.setOnClickListener {
            navigateToHomeActivity()
        }

        binding.buttonRegister.setOnClickListener {
            val username = binding.textInputEditTextName.text.toString()
            val email = binding.textInputEditTextEmail.text.toString().trim()
            val password = binding.textInputEditTextPassword.text.toString()

            viewModel.onRegisterClick(username, email,password)
        }

        binding.textViewLogin.setOnClickListener {
            navigateToLoginFragment()
        }
    }

    override fun setupObservers() {
        viewModel.registerState.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: RegisterState) {
        if (state.isLoading) {
            showLoading()
        } else {
            hideLoading()
        }

        if (state.isRegistrationSuccessful) {
            navigateToHomeActivity()
        }

        state.dataError?.let {
            showError(state.dataError)
        }

        state.validationError?.let {
            showError(state.validationError)
        }
    }

    private fun showLoading() {
        binding.progressBarRegister.show()
        binding.buttonRegister.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBarRegister.hide()
        binding.buttonRegister.isEnabled = true
    }

    private fun showError(error: String) {
        toast(requireContext(), error, false)
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToLoginFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.fragmentContainerViewMainActivity, LoginFragment())
            commit()
        }
    }
}