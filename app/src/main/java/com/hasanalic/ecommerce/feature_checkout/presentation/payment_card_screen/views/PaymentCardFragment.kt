package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hasanalic.ecommerce.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.hasanalic.ecommerce.core.presentation.utils.DateFormatConstants.DATE_FORMAT
import com.hasanalic.ecommerce.databinding.FragmentPaymentCardBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutState
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel
import com.hasanalic.ecommerce.feature_checkout.presentation.ShoppingCartList
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.PaymentCardState
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.PaymentCardViewModel
import com.hasanalic.ecommerce.core.presentation.utils.TimeAndDate
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.utils.PaymentNotificationConstants.CHANNEL_DESCRIPTION_TEXT
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.utils.PaymentNotificationConstants.CHANNEL_ID
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.utils.PaymentNotificationConstants.CHANNEL_NAME
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.utils.PaymentNotificationConstants.NOTIFICATION_ID
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.maskCreditCard
import com.hasanalic.ecommerce.core.utils.show
import com.hasanalic.ecommerce.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

@AndroidEntryPoint
class PaymentCardFragment: Fragment() {

    private var _binding: FragmentPaymentCardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PaymentCardViewModel
    private lateinit var checkoutViewModel: CheckoutViewModel

    private lateinit var randomNumber: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPaymentCardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PaymentCardViewModel::class.java]
        checkoutViewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]
        viewModel.checkIfUserHaveAnyCard()

        createNotificationChannel()

        setupListeners()

        setupObservers()
    }

    private fun setupListeners() {
        binding.toolBarCard.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.buttonConfirm.setOnClickListener {
            if (binding.checkBoxSecure.isChecked) {
                randomNumber = generateRandomNumber().toString()
                showNotification(randomNumber)
                showSecurePopUp()
            } else {
                onClickButtonConfirmByCheckBoxSave()
            }
        }
    }

    private fun onClickButtonConfirmByCheckBoxSave() {
        val cardName = binding.textInputEditTextCardName.text.toString()
        val cardNumber = binding.textInputEditTextCardNumber.text.toString()
        val month = binding.textInputEditTextCardMonth.text.toString()
        val year = binding.textInputEditTextCardYear.text.toString()
        val cvv = binding.textInputEditTextCardCvv.text.toString()

        if (binding.checkBoxSave.isChecked) {
            viewModel.onClickConfirmWithSaveCard(cardName, cardNumber, month, year, cvv)
        } else {
            viewModel.onClickConfirm(cardName, cardNumber, month, year, cvv)
        }
    }

    private fun setupObservers() {
        viewModel.paymentCardState.observe(viewLifecycleOwner) {
            handlePaymentCardState(it)
        }

        checkoutViewModel.checkoutState.observe(viewLifecycleOwner) {
            handleCheckoutState(it)
        }
    }

    private fun handlePaymentCardState(state: PaymentCardState) {
        if (state.isLoading) {
            binding.progressBarPaymentCard.show()
        } else {
            binding.progressBarPaymentCard.hide()
        }

        if (state.doesUserHaveCards) {
            showAvailableCardsPopUp()
        }

        if (state.canUserContinueToNextStep) {
            if (state.cardId != null) {
                checkoutViewModel.buyOrderWithSavedCard(state.cardId)
            } else {
                checkoutViewModel.buyOrderWithCard()
            }
        }

        state.validationError?.let {
            toast(requireContext(), it, false)
        }

        state.dataError?.let {
            TODO()
        }

        state.actionError?.let {
            toast(requireContext(), it, false)
        }
    }

    private fun handleCheckoutState(state: CheckoutState) {
        if (state.isLoading) {
            binding.progressBarPaymentCard.show()
        } else {
            binding.progressBarPaymentCard.hide()
        }

        if (state.isPaymentSuccessful) {
            Navigation.findNavController(binding.root).navigate(R.id.action_paymentCardFragment_to_successFragment)
        }

        state.dataError?.let {
            toast(requireContext(), it, false)
        }
    }

    private fun showAvailableCardsPopUp() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_bank_card, null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()
        dialogView.setBackgroundColor(requireActivity().getColor(R.color.white))

        val buttonOk = dialogView.findViewById<Button>(R.id.buttonYes)
        val buttonNo = dialogView.findViewById<Button>(R.id.buttonNo)

        buttonOk.setOnClickListener {
            dialog.dismiss()
            Navigation.findNavController(binding.root).navigate(R.id.action_paymentCardFragment_to_cardsFragment)
        }

        buttonNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSecurePopUp() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_3d_secure,null)
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create()
        dialogView.setBackgroundColor(requireActivity().getColor(R.color.white))

        val textViewCost = dialogView.findViewById<TextView>(R.id.textViewCost)
        val textViewTime = dialogView.findViewById<TextView>(R.id.textViewTime)
        val textViewCardNumber = dialogView.findViewById<TextView>(R.id.textViewCardNumber)
        val editTextSecureCode = dialogView.findViewById<TextInputEditText>(R.id.textInputEditTextCode)

        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val buttonSend = dialogView.findViewById<Button>(R.id.buttonSend)

        textViewCost.text = ShoppingCartList.totalPrice
        textViewTime.text = "${TimeAndDate.getLocalDate(DATE_FORMAT)} ${TimeAndDate.getTime()}"
        textViewCardNumber.text = binding.textInputEditTextCardNumber.text.toString().maskCreditCard()

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSend.setOnClickListener {
            val inputSecureCode = editTextSecureCode.text.toString()
            if (randomNumber == inputSecureCode) {
                onClickButtonConfirmByCheckBoxSave()
            } else {
                toast(requireContext(),"The security code is incorrect, try again.",false)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showNotification(number: String) {
        val notificationManager = getSystemService(requireContext(), NotificationManager::class.java) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Dear ${binding.textInputEditTextCardName.text}")
            .setContentText("You can complete your shopping with the verification code $number.")
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION_TEXT
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
            }
            val notificationManager = getSystemService(requireContext(), NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun generateRandomNumber(): Int {
        val random = Random()
        return random.nextInt(90000) + 10000
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}