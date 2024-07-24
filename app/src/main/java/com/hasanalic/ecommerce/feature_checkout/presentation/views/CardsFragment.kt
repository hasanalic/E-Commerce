package com.hasanalic.ecommerce.feature_checkout.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentCardsBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel
import com.hasanalic.ecommerce.utils.ItemDecoration
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsFragment: Fragment() {

    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CheckoutViewModel

    private val cardsAdapter by lazy {
        CardsAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCardsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBarCard.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]

        setRecyclerView()
        observe()
    }

    private fun observe() {
        viewModel.statusCards.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success ->  {
                    binding.progressBarCards.hide()
                    cardsAdapter.cardList = it.data ?: listOf()
                    cardsAdapter.notifyChanges()
                }
                is Resource.Error -> {
                    binding.progressBarCards.show()
                }
                is Resource.Loading -> {
                    binding.progressBarCards.hide()
                    toast(requireContext(),it.message?:"Hata",false)
                }
            }
        }

        viewModel.statusPayment.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarCards.hide()
                    Navigation.findNavController(binding.root).navigate(R.id.action_cardsFragment_to_successFragment)
                }
                is Resource.Error -> {
                    binding.progressBarCards.hide()
                    toast(requireContext(),it.message?:"hata",false)
                }
                is Resource.Loading -> {
                    binding.progressBarCards.show()
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerViewCards.adapter = cardsAdapter
        binding.recyclerViewCards.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewCards.addItemDecoration(ItemDecoration(40,40,40))

        cardsAdapter.setOnCardClickLisstener {
            viewModel.setOrderTypeAsCardByPaymentIdAndInitialize(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}