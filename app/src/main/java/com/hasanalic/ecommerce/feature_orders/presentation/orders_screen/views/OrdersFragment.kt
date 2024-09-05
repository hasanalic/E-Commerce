package com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.databinding.FragmentOrdersBinding
import com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.OrdersState
import com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.OrdersViewModel
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment: Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OrdersViewModel

    private val orderAdapter by lazy {
        OrderAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        viewModel.getOrders()

        binding.toolBarOrders.setNavigationOnClickListener {
            requireActivity().finish()
        }

        setupRecyclerView()

        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewOrders.adapter = orderAdapter
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewOrders.addItemDecoration(ItemDecoration(30,30,40))

        orderAdapter.setOnOrderClickListener { orderId ->
            val action = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(orderId)
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun setupObservers() {
        viewModel.ordersState.observe(viewLifecycleOwner) {
            handleOrdersState(it)
        }
    }

    private fun handleOrdersState(state: OrdersState) {
        if (state.isLoading) {
            binding.progressBarOrder.show()
        } else {
            binding.progressBarOrder.hide()
        }

        state.orders.let {
            if (it.isEmpty()) {
                binding.emptyOrder.show()
            } else {
                binding.emptyOrder.hide()
                orderAdapter.orderList = it
            }
            orderAdapter.notifyChanges()
        }

        state.dataError?.let {
            TODO()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}