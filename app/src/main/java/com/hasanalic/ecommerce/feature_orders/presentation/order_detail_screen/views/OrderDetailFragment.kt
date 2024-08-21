package com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentOrderDetailBinding
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail
import com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen.OrderDetailState
import com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen.OrderDetailViewModel
import com.hasanalic.ecommerce.utils.Constants.BANK_OR_CREDIT_CARD
import com.hasanalic.ecommerce.utils.Constants.DATE_FORMAT
import com.hasanalic.ecommerce.utils.Constants.ORDER_CANCELLED
import com.hasanalic.ecommerce.utils.Constants.ORDER_CARGO
import com.hasanalic.ecommerce.utils.Constants.ORDER_DELIVERED
import com.hasanalic.ecommerce.utils.Constants.ORDER_PREPARE
import com.hasanalic.ecommerce.utils.Constants.ORDER_RECEIVED
import com.hasanalic.ecommerce.utils.Constants.ORDER_RETURNED
import com.hasanalic.ecommerce.utils.OrderStatus
import com.hasanalic.ecommerce.utils.TimeAndDate
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment: Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OrderDetailViewModel

    private lateinit var userId: String
    private lateinit var orderId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[OrderDetailViewModel::class.java]

        userId = "1"
        orderId = "1"
        viewModel.getOrderDetail(orderId)

        setupListeners()

        setupObservers()
    }

    private fun setupListeners() {
        binding.topAppBarOrderDetail.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.textViewCancelOrReturn.setOnClickListener {
            if (binding.textViewCancelOrReturn.text == requireActivity().getString(R.string.cancel_order)) {
                showCancelOrderWarning(userId, orderId)
            } else {
                showReturnOrderWarning(userId, orderId)
            }
        }
    }

    private fun showCancelOrderWarning(userId: String, orderId: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Siparişi iptal etmek istediğinizden emin misiniz?")
        alertDialogBuilder.setPositiveButton("İptal et") { _, _ ->
            viewModel.updateOrderStatusToCanceled(userId, orderId)
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ -> }
        alertDialogBuilder.create().show()
    }

    private fun showReturnOrderWarning(userId: String, orderId: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Siparişi iade etmek istediğinizden emin misiniz?")
        alertDialogBuilder.setPositiveButton("İade et") { _, _ ->
            viewModel.updateOrderStatusToReturned(userId, orderId)
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ -> }
        alertDialogBuilder.create().show()
    }

    private fun setupObservers() {
        viewModel.orderDetailState.observe(viewLifecycleOwner) {
            handleOrderDetailState(it)
        }
    }

    private fun handleOrderDetailState(orderDetailState: OrderDetailState) {
        if (orderDetailState.isLoading) {
            binding.progressBarOrderDetail.show()
        } else {
            binding.progressBarOrderDetail.hide()
        }

        if (orderDetailState.isOrderStatusUpdateSuccessful) {
            Navigation.findNavController(binding.root).popBackStack()
        }

        orderDetailState.orderDetail?.let {
            setOrderDetails(it)
        }

        orderDetailState.dataError?.let {
            TODO()
        }

        orderDetailState.actionError?.let {
            toast(requireContext(), it, false)
        }
    }

    private fun setOrderDetails(order: OrderDetail) {
        binding.textViewOrderNo.text = order.orderNo.subSequence(0,10).toString().uppercase()
        binding.textViewOrderDate.text = order.date
        binding.textViewOrderTotal.text = order.total
        binding.textViewCargo.text = order.cargo

        val orderStatus = order.status

        if (order.paymentType == BANK_OR_CREDIT_CARD) {
            binding.imageViewPayment.setImageResource(R.drawable.masterpass)
        } else {
            binding.imageViewPayment.hide()
            binding.textViewPayment.text = order.paymentType
        }

        if (orderStatus == ORDER_CANCELLED || orderStatus == ORDER_RETURNED) {
            when(orderStatus) {
                ORDER_CANCELLED -> {
                    binding.textViewCancelOrReturn.text = ""
                    binding.constraint.hide()
                    binding.imageViewOrderInfo.setImageResource(R.drawable.cancel)
                    binding.textViewOrderInfo.text = requireActivity().getString(R.string.order_cancel_info)
                    binding.linearLayoutCancelOrReturn.show()
                    binding.textViewOrderEstimatedDelivery.text = ORDER_CANCELLED
                }
                ORDER_RETURNED -> {
                    binding.textViewCancelOrReturn.text = ""
                    binding.constraint.hide()
                    binding.imageViewOrderInfo.setImageResource(R.drawable.return_order)
                    binding.textViewOrderInfo.text = requireActivity().getString(R.string.order_return_info)
                    binding.linearLayoutCancelOrReturn.show()
                    binding.textViewOrderEstimatedDelivery.text = ORDER_RETURNED
                }
            }
        } else {
            setOrderStatusConstrait(order.timeStamp, order.date)
        }
    }

    private fun setOrderStatusConstrait(orderTimestamp: Long, orderDate: String) {
        val orderCurrentStatus = OrderStatus.getOrderCurrrentStatus(orderTimestamp, System.currentTimeMillis())
        when(orderCurrentStatus) {
            ORDER_RECEIVED -> {
                binding.textViewOrderEstimatedDelivery.text = TimeAndDate.estimatedDeliveryTime(DATE_FORMAT, orderDate)

                binding.textViewCancelOrReturn.text = requireActivity().getString(R.string.cancel_order)
                binding.constraint.show()
                binding.linearLayoutCancelOrReturn.hide()

                binding.stickOne.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_uncompleted))
                binding.stickTwo.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_uncompleted))
                binding.stickThree.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_uncompleted))

                binding.orderTaken.background = requireActivity().getDrawable(R.drawable.shape_status_current)
                binding.orderPrepare.background = requireActivity().getDrawable(R.drawable.shape_status_uncompleted)
                binding.orderReceiveCargo.background = requireActivity().getDrawable(R.drawable.shape_status_uncompleted)
                binding.orderDelivered.background = requireActivity().getDrawable(R.drawable.shape_status_uncompleted)
            }
            ORDER_PREPARE -> {
                binding.textViewOrderEstimatedDelivery.text = TimeAndDate.estimatedDeliveryTime(DATE_FORMAT, orderDate)

                binding.textViewCancelOrReturn.text = requireActivity().getString(R.string.cancel_order)
                binding.constraint.show()
                binding.linearLayoutCancelOrReturn.hide()

                binding.stickOne.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_completed))
                binding.stickTwo.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_uncompleted))
                binding.stickThree.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_uncompleted))

                binding.orderTaken.background = requireActivity().getDrawable(R.drawable.shape_status_completed)
                binding.orderPrepare.background = requireActivity().getDrawable(R.drawable.shape_status_current)
                binding.orderReceiveCargo.background = requireActivity().getDrawable(R.drawable.shape_status_uncompleted)
                binding.orderDelivered.background = requireActivity().getDrawable(R.drawable.shape_status_uncompleted)
            }
            ORDER_CARGO -> {
                binding.textViewOrderEstimatedDelivery.text = TimeAndDate.estimatedDeliveryTime(DATE_FORMAT, orderDate)

                binding.textViewCancelOrReturn.text = requireActivity().getString(R.string.cancel_order)
                binding.constraint.show()
                binding.linearLayoutCancelOrReturn.hide()

                binding.stickOne.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_completed))
                binding.stickTwo.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_completed))
                binding.stickThree.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_uncompleted))

                binding.orderTaken.background = requireActivity().getDrawable(R.drawable.shape_status_completed)
                binding.orderPrepare.background = requireActivity().getDrawable(R.drawable.shape_status_completed)
                binding.orderReceiveCargo.background = requireActivity().getDrawable(R.drawable.shape_status_current)
                binding.orderDelivered.background = requireActivity().getDrawable(R.drawable.shape_status_uncompleted)
            }
            ORDER_DELIVERED -> {
                binding.textViewOrderEstimatedDelivery.text = ORDER_DELIVERED

                binding.textViewCancelOrReturn.text = requireActivity().getString(R.string.return_order)
                binding.constraint.show()
                binding.linearLayoutCancelOrReturn.hide()

                binding.stickOne.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_completed))
                binding.stickTwo.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_completed))
                binding.stickThree.setBackgroundColor(requireActivity().getColor(R.color.color_order_track_completed))

                binding.orderTaken.background = requireActivity().getDrawable(R.drawable.shape_status_completed)
                binding.orderPrepare.background = requireActivity().getDrawable(R.drawable.shape_status_completed)
                binding.orderReceiveCargo.background = requireActivity().getDrawable(R.drawable.shape_status_completed)
                binding.orderDelivered.background = requireActivity().getDrawable(R.drawable.shape_status_current)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}