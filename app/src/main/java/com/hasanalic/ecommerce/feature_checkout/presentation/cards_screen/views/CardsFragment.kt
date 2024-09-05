package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen.views

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
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutState
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel
import com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen.CardsState
import com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen.CardsViewModel
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsFragment: Fragment() {

    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CardsViewModel
    private lateinit var checkoutViewModel: CheckoutViewModel

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
        checkoutViewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]

        viewModel.getUserCards()

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

        cardsAdapter.setOnCardClickLisstener { cardId ->
            checkoutViewModel.buyOrderWithSavedCard(cardId)
        }
    }

    private fun setupObservers() {
        viewModel.cardsState.observe(viewLifecycleOwner) {
            handleCardsState(it)
        }

        checkoutViewModel.checkoutState.observe(viewLifecycleOwner) {
            handleCheckoutState(it)
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
    }

    private fun handleCheckoutState(state: CheckoutState) {
        if (state.isLoading) {
            binding.progressBarCards.show()
        } else {
            binding.progressBarCards.hide()
        }

        if (state.isPaymentSuccessful) {
            Navigation.findNavController(binding.root).navigate(R.id.action_cardsFragment_to_successFragment)
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