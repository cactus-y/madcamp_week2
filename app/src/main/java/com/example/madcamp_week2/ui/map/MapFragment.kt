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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.FragmentMapBinding
import com.example.madcamp_week2.sample.KaraokeOrPost
import com.example.madcamp_week2.sample.SampleKaraoke
import com.example.madcamp_week2.sample.globalKaraokeList
import com.example.madcamp_week2.sample.globalPostList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.GONE
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
//        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        val root: View = binding.root
        markerEventListener = MarkerEventListener(this, binding)
        mapEventListener = MapEventListener(this, binding)
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

    private fun showPopupWindow() {
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.map_notification_popup_window, null)

        val rcv_map_request_list = popupView.findViewById<RecyclerView>(R.id.rcv_map_request_list)
        val tv_map_request_empty_list = popupView.findViewById<TextView>(R.id.tv_map_request_empty_list)

        // when the list is empty, rcv becomes invisible
        if(true) {
            rcv_map_request_list.visibility = View.GONE
            tv_map_request_empty_list.visibility = View.VISIBLE
        }

        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.showAsDropDown(requireView())
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

    class MarkerEventListener(val fragment: MapFragment, val binding: FragmentMapBinding): MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            val bottomNavigationView = fragment.requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView.visibility = View.GONE

            binding.kakaoMapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            val temp = p1!!.itemName.split("/")

            val karaokeName = temp[0]
            val karaokeAddr = temp[1]
            val karaokeRoadAddr = temp[2]
            val karaokeLat = temp[3]
            val karaokeLong = temp[4]
            val karaokePhone = temp[5]

            // visibility setting
            binding.vpMapCardviewContainer.visibility = View.VISIBLE

            // make list here
            val foundKaraoke = globalKaraokeList.find { it.latitude.toString() == karaokeLat && it.longitude.toString() == karaokeLong }
            val karaokeOrPostList = ArrayList<KaraokeOrPost>()
            karaokeOrPostList.add(KaraokeOrPost(foundKaraoke, null))

            globalPostList.forEach {
                if(foundKaraoke == it.karaoke)
                    karaokeOrPostList.add(KaraokeOrPost(null, it))
            }

            // adapter here
            binding.vpMapCardviewContainer.adapter = CardListAdapter(karaokeOrPostList)
            binding.vpMapCardviewContainer.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })

//            binding.cvMapKaraokeInfoContainer.visibility = View.VISIBLE
//            binding.tvMapKaraokeName.text =
//            binding.tvMapKaraokeAddr.text = temp[1]
//            binding.tvMapKaraokeRoadAddr.text = temp[2]
//            if(temp[5] == "") {
//                binding.tvMapKaraokePhone.text = "전화번호가 없어요!"
//                binding.tvMapKaraokePhone.setTextColor(Color.parseColor("#D3D3D3"))
//            } else {
//                binding.tvMapKaraokePhone.text = temp[5]
//                binding.tvMapKaraokePhone.setTextColor(Color.parseColor("#000000"))
//            }
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

    class MapEventListener(val fragment: MapFragment, val binding: FragmentMapBinding): MapViewEventListener {
        override fun onMapViewInitialized(p0: MapView?) {
        }

        override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        }

        override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
            val bottomNavigationView = fragment.requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView.visibility = View.VISIBLE
            binding.vpMapCardviewContainer.visibility = View.GONE
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