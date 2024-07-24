package com.hasanalic.ecommerce.feature_notification.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanalic.ecommerce.databinding.FragmentNotificationBinding
import com.hasanalic.ecommerce.utils.ItemDecoration
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
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

        binding.toolBarNotifications.setNavigationOnClickListener {
            requireActivity().finish()
        }

        setRecyclerView()

        observer()
    }

    private fun observer() {
        viewModel.stateNotificationList.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Loading -> {
                    binding.progressBarNotifications.show()
                }
                is Resource.Success -> {
                    binding.progressBarNotifications.hide()
                    notificationAdapter.notificationList = it.data?.toList()?: listOf()
                    notificationAdapter.notifyChanges()
                }
                is Resource.Error -> {
                    binding.progressBarNotifications.hide()
                    toast(requireContext(),it.message?:"hata",false)
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerViewNotification.adapter = notificationAdapter
        binding.recyclerViewNotification.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewNotification.addItemDecoration(ItemDecoration(40,30,60))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}