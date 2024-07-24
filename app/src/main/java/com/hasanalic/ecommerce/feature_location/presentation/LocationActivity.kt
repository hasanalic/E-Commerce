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
import com.hasanalic.ecommerce.databinding.ActivityLocationBinding
import com.hasanalic.ecommerce.utils.Constants
import com.hasanalic.ecommerce.utils.Constants.REQUEST_CHECK_SETTINGS
import com.hasanalic.ecommerce.utils.ItemDecoration
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.hide
import com.hasanalic.ecommerce.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var viewModel: LocationViewModel

    /*
    private lateinit var auth: FirebaseAuth
    private var userId: String = ANOMIM_USER_ID

     */

    private val locationAdapter by lazy {
        LocationAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            userId = it.uid
        }

         */

        setRecyclerView()

        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        //viewModel.getAddressList(userId)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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
                //viewModel.saveAddress(userId,address, addressTitle)
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.statusAddressList.observe(this) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarLocation.hide()
                    locationAdapter.addressList = it.data ?: listOf()
                    locationAdapter.notifyChanges()
                }
                is Resource.Loading -> {
                    binding.progressBarLocation.show()
                }
                is Resource.Error -> {
                    binding.progressBarLocation.hide()
                    Toast.makeText(this,it.message?:"hata",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.statusSaveAddress.observe(this) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarLocation.hide()
                    Toast.makeText(this,"Adres kaydedildi",Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBarLocation.hide()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressBarLocation.show()
                }
            }
        }

        viewModel.statusDeleteAddress.observe(this) {
            when(it) {
                is Resource.Success -> {
                    binding.progressBarLocation.hide()
                    Toast.makeText(this,"Adres silindi",Toast.LENGTH_SHORT).show()
                    locationAdapter.notifyChanges()
                }
                is Resource.Error -> {
                    binding.progressBarLocation.hide()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressBarLocation.show()
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerViewAddress.adapter = locationAdapter
        binding.recyclerViewAddress.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewAddress.addItemDecoration(ItemDecoration(40,40,40))

        locationAdapter.setOnDeleteClickListener {
            //viewModel.deleteAddress(userId, it)
            locationAdapter.notifyChanges()
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
                        val city = addressList[0].locality
                        val country = addressList[0].countryName
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
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),Constants.REQUEST_CODR)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.REQUEST_CODR) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findLocation()
            } else {
                Toast.makeText(this,"İzin Gerekli", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}