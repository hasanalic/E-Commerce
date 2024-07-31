package com.hasanalic.ecommerce.feature_auth.presentation.login.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentLoginBinding
import com.hasanalic.ecommerce.feature_auth.presentation.login.LoginState
import com.hasanalic.ecommerce.feature_auth.presentation.login.LoginViewModel
import com.hasanalic.ecommerce.feature_auth.presentation.register.views.RegisterFragment
import com.hasanalic.ecommerce.feature_home.presentation.views.HomeActivity
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast

class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: LoginViewModel

    override fun onStart() {
        super.onStart()
        /*
        val currentUser = auth.currentUser

        if (currentUser != null) {
            moveToHomeActivity()
        }
         */
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.buttonGuest.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.textInputEditTextEmail.text.toString()
            val password = binding.textInputEditTextPassword.text.toString()

            viewModel.onLoginClick(email, password)
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

        if (state.isLoading) {
            binding.progressBarLogin.show()
            binding.buttonLogin.isEnabled = false
        } else {
            binding.progressBarLogin.hide()
            binding.buttonLogin.isEnabled = true
        }

        state.validationError?.let {
            toast(requireContext(), it, false)
        }

        state.dataError?.let {
            toast(requireContext(), it, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}