package com.hasanalic.ecommerce.feature_home.presentation.account_screen.views

import android.app.AlertDialog
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

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AccountViewModel

    //private lateinit var cartAlarmScheduler: CartAlarmScheduler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        viewModel.getUserIfLoggedIn()

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
            //cancelCartAlarm()
        }

        binding.textViewDelete.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    private fun showDeleteAccountDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Are you sure you want to delete the account?")
        alertDialogBuilder.setPositiveButton("Delete") { _, _ ->
            viewModel.deleteUser()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> }

        alertDialogBuilder.create().show()
    }

    private fun cancelCartAlarm() {
        val reminderItemCart = ReminderItem(CART_ALARM_INTERVAL_TEST.toLong(),CART_ALARM_REQUEST_CODE)
        //cartAlarmScheduler.cancel(reminderItemCart)
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
            binding.textViewUserName.text = it.userName
            binding.textViewUserEmail.text = it.userEmail
        }

        if (state.isDeletionCompleted) {
            navigateToAuthActivity()
            toast(requireContext(), "Your account has been deleted.", false)
        }

        if (state.isUserLoggedOut) {
            navigateToAuthActivity()
            toast(requireContext(), "You have been logged out.", false)
        }

        if (state.shouldUserMoveToAuthActivity) {
            navigateToAuthActivity()
            toast(requireContext(), "You must log in to your account.", false)
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