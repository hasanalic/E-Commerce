package com.hasanalic.ecommerce.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentLoginBinding
import com.hasanalic.ecommerce.ui.HomeActivity
import com.hasanalic.ecommerce.utils.CustomSharedPreferences
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.toast

class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    /*
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

     */

    private lateinit var signInWith: String

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

        //auth = FirebaseAuth.getInstance()

        binding.buttonGuest.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.buttonLogin.setOnClickListener {
            if (validateFields()) {
                val email = binding.textInputEditTextEmail.text.toString().trim()
                val password = binding.textInputEditTextPassword.text.toString()
                /*
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {result->
                    result.user?.uid?.let { userId ->
                        signInWith = requireActivity().getString(R.string.classic)
                        viewModel.updateUsersShoppingCartEntities(userId, ANOMIM_USER_ID)
                    }
                }.addOnFailureListener {exception ->
                    println(exception.message)
                    toast(requireContext(),exception.message?:"Girilen bilgiler geçersiz",true)
                }

                 */
            }
        }

        binding.textViewRegister.setOnClickListener {
            navigateToRegisterFragment()
        }

        observer()
    }

    private fun observer() {
        viewModel.stateShoppingCartItems.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    moveToHomeActivity(signInWith)
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        if (binding.textInputEditTextEmail.text.toString().isEmpty() ||
            binding.textInputEditTextPassword.text.toString().isEmpty()) {
            toast(requireContext(),"Lütfen, tüm bilgileri eksiksiz doldurun",false)
            return false
        }
        return true
    }

    private fun moveToHomeActivity(signInFrom: String? = null) {
        signInFrom?.let {
            CustomSharedPreferences(requireContext()).setSignInWithSocialMediaType(signInFrom,requireContext())
        }

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