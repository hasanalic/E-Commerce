package com.hasanalic.ecommerce.feature_location.presentation

import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.REQUEST_CHECK_SETTINGS
import com.hasanalic.ecommerce.core.presentation.utils.AlarmConstants.REQUEST_CODR
import com.hasanalic.ecommerce.databinding.ActivityLocationBinding
import com.hasanalic.ecommerce.feature_location.presentation.views.LocationAdapter
import com.hasanalic.ecommerce.core.presentation.utils.ItemDecoration
import com.hasanalic.ecommerce.core.utils.hide
import com.hasanalic.ecommerce.core.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var viewModel: LocationViewModel

    private var userId: String = ANOMIM_USER_ID

    private val locationAdapter by lazy {
        LocationAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        viewModel.getAddressEntityList(userId)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setupListeners()

        setupRecyclerView()

        setupObservers()
    }

    private fun setupListeners() {
        binding.topAppBarHome.setNavigationOnClickListener {
            finish()
        }

        binding.buttonFindLocation.setOnClickListener {
            findLocation()
        }

        binding.buttonSave.setOnClickListener {
            val address = binding.textInputEditTextAddress.text.toString()
            val addressTitle = binding.textInputEditTextAddressTitle.text.toString()

            if (address.isEmpty() || addressTitle.isEmpty()) {
                Toast.makeText(this,"Lütfen alanları doldurun",Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertAddressEntity(userId, address, addressTitle)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewAddress.adapter = locationAdapter
        binding.recyclerViewAddress.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewAddress.addItemDecoration(ItemDecoration(40,40,40))

        locationAdapter.setOnDeleteClickListener { addressId, position ->
            viewModel.deleteAddressEntity(userId, addressId, position)
            locationAdapter.notifyChanges()
        }
    }

    private fun setupObservers() {
        viewModel.locationState.observe(this) { state ->
            handleLocationState(state)
        }
    }

    private fun handleLocationState(state: LocationState) {
        if (state.isLoading) {
            binding.progressBarLocation.show()
            binding.buttonSave.isEnabled = false
            binding.buttonFindLocation.isEnabled = false
        } else {
            binding.progressBarLocation.hide()
            binding.buttonSave.isEnabled = true
            binding.buttonFindLocation.isEnabled = true
        }

        state.addressEntityList.let {
            locationAdapter.addressList = it
            locationAdapter.notifyChanges()
        }

        if (state.isAddressDeletionSuccessful) {
            Toast.makeText(this, "Adres silindi.", Toast.LENGTH_SHORT).show()
        }

        if (state.isAddressInsertionSuccessful) {
            Toast.makeText(this, "Yeni adres eklendi.", Toast.LENGTH_SHORT).show()
            finish()
        }

        state.validationError?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        state.dataError?.let {
            TODO("error text view")
        }

        state.actionError?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun findLocation() {
        val accessFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION

        if (checkSelfPermission(accessFineLocation) != PackageManager.PERMISSION_GRANTED) {
            askForPermission()
        } else {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener { response ->
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addressList: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                        val address = addressList[0].getAddressLine(0)
                        //val city = addressList[0].locality
                        //val country = addressList[0].countryName

                        binding.textInputEditTextAddress.setText(address)
                    }
                }
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Konum ayarları uygun değil, kullanıcıyı düzeltme isteği gönder
                    try {
                        exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Hata durumunda işleme geç
                        sendEx.printStackTrace()
                    }
                }
            }
        }
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODR)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODR) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findLocation()
            } else {
                Toast.makeText(this,"İzin Gerekli", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}