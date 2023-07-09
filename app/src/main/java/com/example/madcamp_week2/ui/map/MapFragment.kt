package com.example.madcamp_week2.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.databinding.FragmentMapBinding
import com.example.madcamp_week2.sample.globalKaraokeList
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.MapViewEventListener

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val ACCESS_FINE_LOCATION = 1000

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var markerEventListener: MarkerEventListener
    private lateinit var mapEventListener: MapEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        markerEventListener = MarkerEventListener(binding)
        mapEventListener = MapEventListener(binding)
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
        binding.kakaoMapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        val lm: LocationManager = this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userNowLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val uLatitude = userNowLocation?.latitude
        val uLongitude = userNowLocation?.longitude

        println("\n\n\n\n${uLatitude}\n\n\n\n")
        println("\n\n\n\n${uLongitude}\n\n\n\n")

        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

        binding.kakaoMapview.setPOIItemEventListener(markerEventListener)
        binding.kakaoMapview.setMapViewEventListener(mapEventListener)

        // sample karaokes
        globalKaraokeList.forEach {
            val marker = MapPOIItem()
            val itemName = it.name + "/" + it.address + "/" + it.roadAddress + "/" + it.latitude.toString() + "/" + it.longitude.toString() + "/" + it.phoneNumber
            marker.itemName = itemName
            marker.mapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
            marker.isShowCalloutBalloonOnTouch = false
            marker.markerType = MapPOIItem.MarkerType.BluePin
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
            binding.kakaoMapview.addPOIItem(marker)
        }

    }

    private fun stopTracking() {
        binding.kakaoMapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
//        binding.flMap.addView(mapView)
    }


    override fun onResume() {
        super.onResume()
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
//        binding.flMap.removeAllViews()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class MarkerEventListener(val binding: FragmentMapBinding): MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            binding.kakaoMapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            val temp = p1!!.itemName.split("/")
            binding.cvMapKaraokeInfoContainer.visibility = View.VISIBLE
            binding.tvMapKaraokeName.text = temp[0]
            binding.tvMapKaraokeAddr.text = temp[1]
            binding.tvMapKaraokeRoadAddr.text = temp[2]
            if(temp[5] == "") {
                binding.tvMapKaraokePhone.text = "전화번호가 없어요!"
                binding.tvMapKaraokePhone.setTextColor(Color.parseColor("#D3D3D3"))
            } else {
                binding.tvMapKaraokePhone.text = temp[5]
                binding.tvMapKaraokePhone.setTextColor(Color.parseColor("#000000"))
            }
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {
        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        }

    }

    class MapEventListener(val binding: FragmentMapBinding): MapViewEventListener {
        override fun onMapViewInitialized(p0: MapView?) {
        }

        override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        }

        override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
            binding.cvMapKaraokeInfoContainer.visibility = View.GONE
        }

        override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        }

    }

}