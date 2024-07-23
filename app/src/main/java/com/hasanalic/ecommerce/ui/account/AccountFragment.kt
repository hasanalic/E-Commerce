package com.hasanalic.ecommerce.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hasanalic.ecommerce.databinding.FragmentAccountBinding
import com.hasanalic.ecommerce.ui.CustomerService
import com.hasanalic.ecommerce.ui.FeedBackActivity
import com.hasanalic.ecommerce.ui.HomeActivity
import com.hasanalic.ecommerce.ui.MainActivity
import com.hasanalic.ecommerce.ui.OrderActivity
import com.hasanalic.ecommerce.utils.Constants.CART_ALARM_INTERVAL_TEST
import com.hasanalic.ecommerce.utils.Constants.CART_ALARM_REQUEST_CODE
import com.hasanalic.ecommerce.utils.CustomSharedPreferences
import com.hasanalic.ecommerce.utils.notification.ReminderItem
import com.hasanalic.ecommerce.utils.notification.cart.CartAlarmScheduler

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private var homeActivity: HomeActivity? = null

    /*
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

     */

    private lateinit var cartAlarmScheduler: CartAlarmScheduler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onStart() {
        super.onStart()
        homeActivity?.hideToolBar()

        /*
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ANOMIM_USER_ID
        cartAlarmScheduler = CartAlarmScheduler(requireContext(), userId)

        if (currentUser == null) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            toast(requireContext(),"Hesap bilgilerini görüntülemek için hesabınıza giriş yapınız.",false)
            requireActivity().finish()
        }

         */
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser?.let {currentUser ->
            binding.textViewUserEmail.text = currentUser.email
        }
         */

        val signInFrom = CustomSharedPreferences(requireContext()).getSignInWithSocialMediaType(requireContext())?:""

        binding.textViewSupport.setOnClickListener {
            val intent = Intent(requireContext(),CustomerService::class.java)
            startActivity(intent)
        }

        binding.textViewOrders.setOnClickListener {
            val intent = Intent(requireContext(),OrderActivity::class.java)
            startActivity(intent)
        }

        binding.textViewFeedBack.setOnClickListener {
            val intent = Intent(requireContext(),FeedBackActivity::class.java)
            startActivity(intent)
        }

        binding.textViewLogout.setOnClickListener {
            signOutAndNavigateToLoginFragment(signInFrom)
            cancelCartAlarm()
        }
    }

    private fun cancelCartAlarm() {
        val reminderItemCart = ReminderItem(CART_ALARM_INTERVAL_TEST.toLong(),CART_ALARM_REQUEST_CODE)
        cartAlarmScheduler.cancel(reminderItemCart)
    }

    private fun signOutAndNavigateToLoginFragment(signInFrom: String) {
        //mAuth.signOut()
    }

    private fun moveToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}