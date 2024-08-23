package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.databinding.FragmentCardsBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen.CardsState
import com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen.CardsViewModel
import com.hasanalic.ecommerce.utils.ItemDecoration
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsFragment: Fragment() {

    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CardsViewModel

    private val cardsAdapter by lazy {
        CardsAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCardsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CardsViewModel::class.java]

        setupListeners()

        setupRecyclerView()

        setupObservers()
    }

    private fun setupListeners() {
        binding.toolBarCard.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCards.adapter = cardsAdapter
        binding.recyclerViewCards.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewCards.addItemDecoration(ItemDecoration(40,40,40))

        cardsAdapter.setOnCardClickLisstener {
            //viewModel.setOrderTypeAsCardByPaymentIdAndInitialize(it)
        }
    }

    private fun setupObservers() {
        viewModel.cardsState.observe(viewLifecycleOwner) {
            handleCardsState(it)
        }
    }

    private fun handleCardsState(state: CardsState) {
        if (state.isLoading) {
            binding.progressBarCards.show()
        } else {
            binding.progressBarCards.hide()
        }

        state.cardList.let {
            cardsAdapter.cardList = it
            cardsAdapter.notifyChanges()
        }

        state.dataError?.let {
            TODO()
        }

        TODO("CHECKOUT PROCESS")
        /*
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
         */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}