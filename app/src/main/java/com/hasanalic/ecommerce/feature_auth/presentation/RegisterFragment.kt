package com.hasanalic.ecommerce.feature_auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentRegisterBinding
import com.hasanalic.ecommerce.feature_home.presentation.views.HomeActivity
import com.hasanalic.ecommerce.utils.CustomSharedPreferences
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.toast

class RegisterFragment: Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    /*
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var callbackManager: CallbackManager

     */

    private lateinit var signInWith: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

        /*
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
         */

        binding.buttonGuest.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.buttonRegister.setOnClickListener {
            if (validateFields()) {
                val username = binding.textInputEditTextName.text.toString()
                val email = binding.textInputEditTextEmail.text.toString().trim()
                val password = binding.textInputEditTextPassword.text.toString()

                /*
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {result ->
                    result.user?.uid?.let {uid ->
                        createUserOnFirestore(uid, username,requireActivity().getString(R.string.classic))
                    }
                }.addOnFailureListener {exception ->
                    println(exception.message)
                    toast(requireContext(),exception.message?:"Girilen bilgiler geçersiz",true)
                }

                 */
            }
        }

        binding.textViewLogin.setOnClickListener {
            navigateToLoginFragment()
        }

        observer()
    }

    private fun observer() {
        viewModel.stateShoppingCartItems.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    toast(requireContext(),"Kayıt işlemi başarılı.",false)
                    moveToHomeActivity(signInWith)
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun moveToHomeActivity(signInFrom: String? = null) {
        signInFrom?.let {
            CustomSharedPreferences(requireContext()).setSignInWithSocialMediaType(signInFrom,requireContext())
        }

        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun validateFields(): Boolean {
        if (binding.textInputEditTextEmail.text.toString().isEmpty() ||
            binding.textInputEditTextPassword.text.toString().isEmpty() ||
            binding.textInputEditTextName.text.toString().isEmpty()) {
            toast(requireContext(),"Lütfen, tüm bilgileri eksiksiz doldurun",false)
            return false
        }
        return true
    }

    private fun navigateToLoginFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.fragmentContainerViewMainActivity, LoginFragment())
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}