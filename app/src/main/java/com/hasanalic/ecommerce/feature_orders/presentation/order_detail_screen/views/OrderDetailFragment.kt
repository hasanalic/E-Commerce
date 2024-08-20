package com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentOrderDetailBinding
import com.hasanalic.ecommerce.feature_orders.presentation.orders_screen.OrdersViewModel
import com.hasanalic.ecommerce.utils.Constants.DATE_FORMAT
import com.hasanalic.ecommerce.utils.Constants.ORDER_CARGO
import com.hasanalic.ecommerce.utils.Constants.ORDER_DELIVERED
import com.hasanalic.ecommerce.utils.Constants.ORDER_PREPARE
import com.hasanalic.ecommerce.utils.Constants.ORDER_RECEIVED
import com.hasanalic.ecommerce.utils.OrderStatus
import com.hasanalic.ecommerce.utils.TimeAndDate
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment: Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: OrdersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]

        /*
        val order = OrderSingleton.order
        order?.let {
            viewModel.getAddressDetail(it.orderUserId,it.orderAddressId)

            if (it.orderPaymentType == BANK_OR_CREDIT_CARD) {
                viewModel.getPaymentDetail(it.orderUserId,it.orderPaymentId.toString())
            }

            binding.textViewCancelOrReturn.setOnClickListener {
                if (binding.textViewCancelOrReturn.text == requireActivity().getString(R.string.cancel_order)) {
                    showCancelOrderWarning(order.orderUserId, order.orderId)
                } else {
                    showReturnOrderWarning(order.orderUserId, order.orderId)
                }
            }

            setOrderDetails(it)
        }

         */

        binding.topAppBarOrderDetail.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        //observe()
    }

    /*
    private fun observe() {
        viewModel.statusAddress.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.textViewAddressDetail.text = it.data!!.addressDetail
                    viewModel.resetAddressDetail()
                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"hata",false)
                }
                is Resource.Loading -> {}
            }
        }

        viewModel.statusPayment.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.textViewPayment.text = it.data!!.paymentCardNumber.toString().maskCreditCard()
                    viewModel.resetPaymentDetail()
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {}
            }
        }

        viewModel.statusUpdateOrderStatus.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    toast(requireContext(),it.data!!,false)
                    viewModel.resetUpdateOrderStatus()
                    Navigation.findNavController(binding.root).popBackStack()
                }
                is Resource.Error -> {
                    toast(requireContext(),it.message?:"hata",false)
                }
                is Resource.Loading -> {}
            }
        }
    }

     */

    /*
    private fun setOrderDetails(order: Order) {
        binding.textViewOrderNo.text = order.orderNo.subSequence(0,10).toString().uppercase()
        binding.textViewOrderDate.text = order.orderDate
        binding.textViewOrderTotal.text = order.orderTotal
        binding.textViewCargo.text = order.orderCargo

        val orderStatus = order.orderStatus

        if (order.orderPaymentType == BANK_OR_CREDIT_CARD) {
            binding.imageViewPayment.setImageResource(R.drawable.masterpass)
        } else {
            binding.imageViewPayment.hide()
            binding.textViewPayment.text = order.orderPaymentType
        }

        if (orderStatus == ORDER_CANCELLED || orderStatus == ORDER_RETURN) {
            when(orderStatus) {
                ORDER_CANCELLED -> {
                    binding.textViewCancelOrReturn.text = ""
                    binding.constraint.hide()
                    binding.imageViewOrderInfo.setImageResource(R.drawable.cancel)
                    binding.textViewOrderInfo.text = requireActivity().getString(R.string.order_cancel_info)
                    binding.linearLayoutCancelOrReturn.show()
                    binding.textViewOrderEstimatedDelivery.text = ORDER_CANCELLED
                }
                ORDER_RETURN -> {
                    binding.textViewCancelOrReturn.text = ""
                    binding.constraint.hide()
                    binding.imageViewOrderInfo.setImageResource(R.drawable.return_order)
                    binding.textViewOrderInfo.text = requireActivity().getString(R.string.order_return_info)
                    binding.linearLayoutCancelOrReturn.show()
                    binding.textViewOrderEstimatedDelivery.text = ORDER_RETURN
                }
            }
        } else {
            setOrderStatusConstrait(order.orderTimeStamp, order.orderDate)
        }
    }

     */

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

    /*
    private fun showCancelOrderWarning(userId: String, orderId: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Siparişi iptal etmek istediğinizden emin misiniz?")
        alertDialogBuilder.setPositiveButton("İptal et") { _, _ ->
            viewModel.updateOrderStatusToCancel(userId, orderId)
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ ->

        }

        alertDialogBuilder.create().show()
    }

     */

    /*
    private fun showReturnOrderWarning(userId: String, orderId: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Siparişi iade etmek istediğinizden emin misiniz?")
        alertDialogBuilder.setPositiveButton("İade et") { _, _ ->
            viewModel.updateOrderStatusToReturn(userId, orderId)
        }
        alertDialogBuilder.setNegativeButton("Vazgeç") { _, _ ->

        }

        alertDialogBuilder.create().show()
    }

     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}