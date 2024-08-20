package com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.databinding.FragmentOrdersBinding
import com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment: Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OrdersViewModel

    //private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    /*
    private val orderAdapter by lazy {
        OrderAdapter()
    }

     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            userId = it.uid
        }

         */

        viewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        //viewModel.getOrderList(userId)

        binding.toolBarOrders.setNavigationOnClickListener {
            requireActivity().finish()
        }

        setRecyclerView()

        //observe()
    }

    /*
    private fun observe() {
        viewModel.statusOrderList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarOrder.hide()
                    val orderList = it.data?.toList()

                    if (orderList.isNullOrEmpty()) {
                        orderAdapter.orderList = listOf()
                        binding.emptyOrder.show()
                    } else {
                        binding.emptyOrder.hide()
                        orderAdapter.orderList = orderList
                    }
                    orderAdapter.notifyChanges()
                }
                is Resource.Error -> {
                    binding.progressBarOrder.hide()
                    toast(requireContext(),it.message?:"hata",false)
                }
                is Resource.Loading -> {
                    binding.progressBarOrder.show()
                }
            }
        }
    }

     */

    private fun setRecyclerView() {
        /*
        binding.recyclerViewOrders.adapter = orderAdapter
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewOrders.addItemDecoration(ItemDecoration(30,30,40))

        orderAdapter.setOnOrderClickListener {
            OrderSingleton.order = it
            Navigation.findNavController(binding.root).navigate(R.id.action_ordersFragment_to_orderDetailFragment)
        }

         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}