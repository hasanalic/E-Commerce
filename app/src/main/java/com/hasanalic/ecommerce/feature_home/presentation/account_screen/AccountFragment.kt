package com.hasanalic.ecommerce.feature_home.presentation.account_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.CART_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.CART_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.databinding.FragmentAccountBinding
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_orders.presentation.OrderActivity
import com.hasanalic.ecommerce.notification.ReminderItem
import com.hasanalic.ecommerce.notification.cart.CartAlarmScheduler

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAlarmScheduler: CartAlarmScheduler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewSupport.setOnClickListener {
            //val intent = Intent(requireContext(), CustomerService::class.java)
            //startActivity(intent)
        }

        binding.textViewOrders.setOnClickListener {
            val intent = Intent(requireContext(), OrderActivity::class.java)
            startActivity(intent)
        }

        binding.textViewFeedBack.setOnClickListener {
            //val intent = Intent(requireContext(), FeedBackActivity::class.java)
            //startActivity(intent)
        }

        binding.textViewLogout.setOnClickListener {
            signOutAndNavigateToLoginFragment()
            cancelCartAlarm()
        }
    }

    private fun cancelCartAlarm() {
        val reminderItemCart = ReminderItem(CART_ALARM_INTERVAL_TEST.toLong(),CART_ALARM_REQUEST_CODE)
        cartAlarmScheduler.cancel(reminderItemCart)
    }

    private fun signOutAndNavigateToLoginFragment() {

    }

    private fun moveToMainActivity() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}