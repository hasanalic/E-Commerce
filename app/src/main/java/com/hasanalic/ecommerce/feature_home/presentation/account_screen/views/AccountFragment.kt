package com.hasanalic.ecommerce.feature_home.presentation.account_screen.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.CART_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.CART_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
import com.hasanalic.ecommerce.databinding.FragmentAccountBinding
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import com.hasanalic.ecommerce.feature_home.presentation.account_screen.AccountState
import com.hasanalic.ecommerce.feature_home.presentation.account_screen.AccountViewModel
import com.hasanalic.ecommerce.feature_orders.presentation.OrderActivity
import com.hasanalic.ecommerce.notification.ReminderItem
import com.hasanalic.ecommerce.notification.cart.CartAlarmScheduler

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AccountViewModel

    private lateinit var cartAlarmScheduler: CartAlarmScheduler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        viewModel.getUser()

        setupListeners()

        setupObserver()
    }

    private fun setupListeners() {
        binding.textViewOrders.setOnClickListener {
            val intent = Intent(requireContext(), OrderActivity::class.java)
            startActivity(intent)
        }

        binding.textViewLogout.setOnClickListener {
            viewModel.logOutUser()
            cancelCartAlarm()
        }
    }

    private fun cancelCartAlarm() {
        val reminderItemCart = ReminderItem(CART_ALARM_INTERVAL_TEST.toLong(),CART_ALARM_REQUEST_CODE)
        cartAlarmScheduler.cancel(reminderItemCart)
    }

    private fun setupObserver() {
        viewModel.accountState.observe(viewLifecycleOwner) {
            handleAccountState(it)
        }
    }

    private fun handleAccountState(state: AccountState) {
        if (state.isLoading) {
            binding.progressBarAccount.show()
        } else {
            binding.progressBarAccount.hide()
        }

        state.user?.let {

        }

        if (state.isUserLoggedOut) {
            navigateToAuthActivity()
        }

        state.actionError?.let {
            toast(requireContext(), it, false)
        }
    }

    private fun navigateToAuthActivity() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}