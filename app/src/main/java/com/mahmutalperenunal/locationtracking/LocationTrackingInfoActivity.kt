package com.mahmutalperenunal.locationtracking

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.mahmutalperenunal.locationtracking.databinding.ActivityLocationTrackingInfoBinding
import java.io.IOException
import java.util.*

class LocationTrackingInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationTrackingInfoBinding

    //private var userId: Int = 0
    //private var userName: String = ""

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationTrackingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup toolbar
        binding.locationTrackingInfoToolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(binding.locationTrackingInfoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //userId = intent.getIntExtra("Student ID", 0)
        //userName = intent.getStringExtra("Student Names").toString()

        //binding.locationTrackingInfoUserNameTextView.text = userName

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        checkConnection()
    }


    /**
     * check location permission
     */
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkGPS()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }


    /**
     * check GPS
     */
    private fun checkGPS() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(this.applicationContext)
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->

            //gps on or off
            try {

                task.getResult(ApiException::class.java)

                getChildLocation()

            } catch (e: ApiException) {

                e.printStackTrace()

                when (e.statusCode) {

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {

                        //send request for enable the GPS
                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(this, 200)

                    } catch (sendIntentException: IntentSender.SendIntentException) {
                        Log.e("GPS", sendIntentException.toString())
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        //when the settings unavailable
                    }

                }

            }

        }
    }


    /**
     * get current location
     */
    private fun getChildLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->

            val location = task.result

            if (location != null) {

                try {

                    val geocoder = Geocoder(this, Locale.getDefault())

                    val address = geocoder.getFromLocation(location.latitude, location.latitude, 1)

                    //set the address in textview
                    val addressLine = address!![0].getAddressLine(0)
                    binding.moduleStudentTrackingInfoDescriptionTextView.text = addressLine

                    Log.d("Address", addressLine)

                    val addressLocation = address[0].getAddressLine(0)

                    openLocation(addressLocation.toString())

                } catch (e: IOException) {
                    Log.e("GPS", e.toString())
                }

            }

        }

    }


    /**
     * open location on maps
     */
    private fun openLocation(location: String) {
        val uri = Uri.parse("geo:0, 0?q=$location")

        binding.locationTrackingInfoLocationTextButton.setOnClickListener {
            if (binding.moduleStudentTrackingInfoDescriptionTextView.text.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }


    /**
     * check network connection
     */
    private fun checkConnection() {

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (!isConnected) {
                AlertDialog.Builder(this, R.style.CustomAlertDialog)
                    .setTitle("İnternet Bağlantısı Yok")
                    .setMessage("Lütfen internet bağlantınızı kontrol edin!")
                    .setIcon(R.drawable.without_internet)
                    .setNegativeButton("Tamam") { dialog, _ ->
                        checkConnection()
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}