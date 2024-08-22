package com.hasanalic.ecommerce.feature_checkout.presentation.checkout_end_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.hasanalic.ecommerce.databinding.FragmentSuccessBinding
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SuccessFragment: Fragment() {

    @Inject
    lateinit var homeRepository: HomeRepository

    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!

    //private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSuccessBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            binding.buttonPreviewOrder.text = "Ana Sayfaya Dön"
            binding.buttonPreviewOrder.setOnClickListener {
                requireActivity().finish()
            }
        } else {
            binding.buttonPreviewOrder.text = "Siparişleri Görüntüle"
            binding.buttonPreviewOrder.setOnClickListener {
                val intent = Intent(requireActivity(),OrderActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}