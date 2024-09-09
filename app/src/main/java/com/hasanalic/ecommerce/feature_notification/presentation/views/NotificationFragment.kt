package com.hasanalic.ecommerce.feature_notification.presentation.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.databinding.FragmentNotificationBinding
import com.hasanalic.ecommerce.feature_notification.presentation.NotificationState
import com.hasanalic.ecommerce.feature_notification.presentation.NotificationViewModel
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
import com.hasanalic.ecommerce.feature_auth.presentation.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationViewModel

    private val notificationAdapter by lazy {
        NotificationAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNotificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NotificationViewModel::class.java]
        viewModel.getNotificationsIfUserLoggedIn()

        setupListeners()

        setupRecyclerView()

        setupObservers()
    }

    private fun setupListeners() {
        binding.toolBarNotifications.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewNotification.adapter = notificationAdapter
        binding.recyclerViewNotification.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewNotification.addItemDecoration(ItemDecoration(40,30,60))
    }

    private fun setupObservers() {
        viewModel.notificationState.observe(viewLifecycleOwner) { notificationState ->
            handleNotificationState(notificationState)
        }
    }

    private fun handleNotificationState(state: NotificationState) {
        if (state.isLoading) {
            binding.progressBarNotifications.show()
        } else {
            binding.progressBarNotifications.hide()
        }

        if (!state.isUserLoggedIn) {
            navigateToAuthActivityAndFinish()
        }

        state.notificationList.let {
            notificationAdapter.notificationList = it
            notificationAdapter.notifyChanges()
        }

        state.dataError?.let {
            TODO()
        }
    }

    private fun navigateToAuthActivityAndFinish() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        startActivity(intent)
        toast(requireContext(),"Log in to your account to view notifications.",false)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}