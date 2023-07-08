package com.example.madcamp_week2.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.databinding.FragmentMapBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val ACCESS_FINE_LOCATION = 1000

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        mapView = MapView(context)
//
//        if(checkLocationService()) {
//            permissionCheck()
//            stopTracking()
//        } else {
//            Toast.makeText(context, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
//        }



        return root
    }

//    @SuppressLint("MissingPermission")
//    private fun startTracking() {
//        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
//
//        val lm: LocationManager = this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val userNowLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//
//        val uLatitude = userNowLocation?.latitude
//        val uLongitude = userNowLocation?.longitude
//
//        println("\n\n\n\n${uLatitude}\n\n\n\n")
//        println("\n\n\n\n${uLongitude}\n\n\n\n")
//
//        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
//
//        val marker = MapPOIItem()
//        marker.itemName = "Current location"
//        marker.mapPoint = uNowPosition
//        marker.markerType = MapPOIItem.MarkerType.BluePin
//        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
//        mapView.addPOIItem(marker)
//        binding.flMap.addView(mapView)
//    }



    private fun permissionCheck() {
        val preference = this.requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                val builder = AlertDialog.Builder(this.requireContext())
                builder.setMessage("현재 위치를 확인하려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if(isFirstCheck) {
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    var builder = AlertDialog.Builder(this.requireContext())
                    builder.setMessage("현재 위치를 확인하려면 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${this.requireActivity().packageName}"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
            startTracking()
        }
    }



    private fun checkLocationService(): Boolean {
        val locationManager = this.requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun startTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        val lm: LocationManager = this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude

        println("\n\n\n\n${uLatitude}\n\n\n\n")
        println("\n\n\n\n${uLongitude}\n\n\n\n")

        binding.flMap.addView(mapView)
    }

    private fun stopTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
//        binding.flMap.addView(mapView)
    }


    override fun onResume() {
        super.onResume()
        mapView = MapView(context)
        if(checkLocationService()) {
            permissionCheck()
//            stopTracking()
        } else {
            Toast.makeText(context, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
        }
//        binding.flMap.addView(mapView)
    }



    override fun onPause() {
        super.onPause()
        stopTracking()
        binding.flMap.removeAllViews()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}