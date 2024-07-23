package com.hasanalic.ecommerce.ui.home.views

import android.content.Context
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
import com.hasanalic.ecommerce.ui.HomeActivity
import com.hasanalic.ecommerce.ui.ProductDetailActivity
import com.hasanalic.ecommerce.ui.home.HomeViewModel
import com.hasanalic.ecommerce.ui.home.SharedViewModel
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.ItemDecoration
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

    /*
    private lateinit var auth: FirebaseAuth

     */
    private var userId: String = Constants.ANOMIM_USER_ID

    private var homeActivity: HomeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onStart() {
        super.onStart()
        homeActivity?.hideToolBar()
    }

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
            viewModel.removeFromComparedProductList(productId)
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
        viewModel.getComparedProductList()
        viewModel.getShoppingCartCount(userId)

        setRecyclerView()

        observe()
    }

    private fun observe() {
        viewModel.stateComparedProductList.observe(viewLifecycleOwner) {
            comparedAdapter.comparedProducts = it
        }

        viewModel.stateShoppingCartItemSize.observe(viewLifecycleOwner) {
            sharedViewModel.updateCartItemCount(it)
        }
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
        viewModel.getShoppingCartCount(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}