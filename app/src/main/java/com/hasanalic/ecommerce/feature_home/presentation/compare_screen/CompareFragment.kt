package com.hasanalic.ecommerce.feature_home.presentation.compare_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.databinding.FragmentCompareBinding
import com.hasanalic.ecommerce.feature_product_detail.presentation.ProductDetailActivity
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.HomeViewModel
import com.hasanalic.ecommerce.feature_home.presentation.SharedViewModel
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompareFragment: Fragment() {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private lateinit var sharedViewModel: SharedViewModel

    private val comparedAdapter by lazy {
        CompareAdapter()
    }

    private var userId: String = ANOMIM_USER_ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompareBinding.inflate(inflater)
        return binding.root
    }

    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val productId = comparedAdapter.comparedProducts[viewHolder.layoutPosition].productId
            //viewModel.removeFromComparedProductList(productId)
            comparedAdapter.notifyChanges()
        }
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

        binding.toolBarCompare.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        //viewModel.getComparedProductList()

        setRecyclerView()

        observe()
    }

    private fun observe() {
        /*
        viewModel.stateComparedProductList.observe(viewLifecycleOwner) {
            comparedAdapter.comparedProducts = it
        }

        viewModel.stateShoppingCartItemSize.observe(viewLifecycleOwner) {
            sharedViewModel.updateCartItemCount(it)
        }

         */
    }

    private fun setRecyclerView() {
        binding.recyclerViewCompare.adapter = comparedAdapter
        binding.recyclerViewCompare.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewCompare.addItemDecoration(ItemDecoration(0,0,50))
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewCompare)

        comparedAdapter.setOnComparedProductListener {
            val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
            intent.putExtra(getString(R.string.product_id),it)
            launcherToProductDetail.launch(intent)
        }
    }

    private val launcherToProductDetail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //viewModel.getShoppingCartCount(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}