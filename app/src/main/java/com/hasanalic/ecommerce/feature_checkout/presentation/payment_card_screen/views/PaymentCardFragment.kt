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
import com.hasanalic.ecommerce.databinding.FragmentPaymentCardBinding
import com.hasanalic.ecommerce.feature_checkout.presentation.CheckoutViewModel
import com.hasanalic.ecommerce.feature_checkout.presentation.ShoppingCartList
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.PaymentCardState
import com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen.PaymentCardViewModel
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.Constants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.utils.Constants.CHANNEL_DESCRIPTION_TEXT
import com.hasanalic.ecommerce.utils.Constants.CHANNEL_ID
import com.hasanalic.ecommerce.utils.Constants.CHANNEL_NAME
import com.hasanalic.ecommerce.utils.Constants.NOTIFICATION_ID
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.TimeAndDate
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.maskCreditCard
import com.hasanalic.ecommerce.utils.show
import com.hasanalic.ecommerce.utils.toast
import java.util.Random

class PaymentCardFragment: Fragment() {

    private var _binding: FragmentPaymentCardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PaymentCardViewModel

    private var userId: String = ANOMIM_USER_ID

    private lateinit var randomNumber: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPaymentCardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PaymentCardViewModel::class.java]
        viewModel.checkIfUserHaveAnyCard(userId)

        createNotificationChannel()

        setupListeners()

        setupObservers()

        observe()
    }

    private fun setupListeners() {
        binding.toolBarCard.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        binding.buttonConfirm.setOnClickListener {
            if (validateFields()) {
                if (binding.checkBoxSecure.isChecked) {
                    randomNumber = generateRandomNumber().toString()
                    showNotification(randomNumber)
                    showSecurePopUp()
                } else {
                    if (binding.checkBoxSave.isChecked) {
                        val cardName = binding.textInputEditTextCardName.text.toString()
                        val cardNumber = binding.textInputEditTextCardNumber.text.toString()

                        //viewModel.setOrderTypeAsCardAndSaveCardAndInitialize(cardName,cardNumber)
                    } else {
                        //viewModel.setOrderTypeAsCardAndInitialize()
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.paymentCardState.observe(viewLifecycleOwner) {

        }
    }

    private fun handlePaymentCardState(state: PaymentCardState) {
        if (state.isLoading) {
            binding.progressBarPaymentCard.show()
        } else {
            binding.progressBarPaymentCard.hide()
        }

        if (state.doesUserHaveCards) {

        }
    }

    private fun observe() {
        /*
        viewModel.statusPayment.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarPaymentCard.hide()
                    Navigation.findNavController(binding.root).navigate(R.id.action_cardFragment_to_successFragment)
                }
                is Resource.Error -> {
                    binding.progressBarPaymentCard.hide()
                    toast(requireContext(),it.message?:"hata",false)
                }
                is Resource.Loading -> {
                    binding.progressBarPaymentCard.show()
                }
            }
        }

        viewModel.statusCards.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarPaymentCard.hide()
                    if (!(it.data.isNullOrEmpty())) {
                        showAvailableCardsPopUp()
                    }
                }
                is Resource.Error -> {
                    binding.progressBarPaymentCard.hide()
                    toast(requireContext(),it.message?:"hata",false)
                }
                is Resource.Loading -> {
                    binding.progressBarPaymentCard.show()
                }
            }
        }

         */
    }

    private fun validateFields(): Boolean {
        if (binding.textInputEditTextCardMonth.text.toString().isEmpty() ||
            binding.textInputEditTextCardYear.text.toString().isEmpty() ||
            binding.textInputEditTextCardCvv.text.toString().isEmpty() ||
            binding.textInputEditTextCardName.text.toString().isEmpty() ||
            binding.textInputEditTextCardNumber.text.toString().isEmpty()) {
            toast(requireContext(),"Lütfen, tüm bilgileri eksiksiz doldurun",false)
            return false
        } else if (binding.textInputEditTextCardNumber.text.toString().length != 16) {
            toast(requireContext(),"Lütfen, kart numarasını kontrol ediniz",false)
            return false
        }
        return true
    }

    private fun showAvailableCardsPopUp() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_masterpass, null)
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
        textViewTime.text = "${TimeAndDate.getLocalDate(Constants.DATE_FORMAT)} ${TimeAndDate.getTime()}"
        textViewCardNumber.text = binding.textInputEditTextCardNumber.text.toString().maskCreditCard()

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSend.setOnClickListener {
            val inputSecureCode = editTextSecureCode.text.toString()
            if (randomNumber == inputSecureCode) {
                if (binding.checkBoxSave.isChecked) {
                    val cardName = binding.textInputEditTextCardName.text.toString()
                    val cardNumber = binding.textInputEditTextCardNumber.text.toString()
                    //viewModel.setOrderTypeAsCardAndSaveCardAndInitialize(cardName,cardNumber)
                } else {
                    //viewModel.setOrderTypeAsCardAndInitialize()
                }
            } else {
                toast(requireContext(),"Güvenlik kodu yanlış, tekrar deneyiniz.",false)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showNotification(number: String) {
        val notificationManager = getSystemService(requireContext(), NotificationManager::class.java) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Sayın ${binding.textInputEditTextCardName.text}")
            .setContentText("$number doğrulama kodu ile alışverişinizi tamamlayabilirsiniz.")
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