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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.auth0.android.jwt.JWT
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.KaraokeOrBoard
import com.example.madcamp_week2.api.data.board.GetBoardListResponseBody
import com.example.madcamp_week2.api.data.karaoke.GetKaraokeListResponseBody
import com.example.madcamp_week2.api.data.karaoke.Karaoke
import com.example.madcamp_week2.databinding.FragmentMapBinding
import com.example.madcamp_week2.getUserToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.MapViewEventListener
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val ACCESS_FINE_LOCATION = 1000

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var karaokeList = ArrayList<Karaoke>()
//    private var karaokeOrGuestList = ArrayList<KaraokeOrGuest>()

    private lateinit var userToken: JWT

    private lateinit var markerEventListener: MarkerEventListener
    private lateinit var mapEventListener: MapEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userToken = getUserToken(requireContext())
        markerEventListener = MarkerEventListener(this, binding, karaokeList)
        mapEventListener = MapEventListener(this, binding)

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

        callKaraokeListAPI(uLatitude.toString(), uLongitude.toString())

    }

    private fun stopTracking() {
        binding.kakaoMapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
//        binding.flMap.addView(mapView)
    }

    override fun onResume() {
        super.onResume()
        val navbar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        if(navbar.visibility == View.GONE) {
            navbar.visibility = View.VISIBLE
        }
        userToken = getUserToken(requireContext())
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
        binding.kakaoMapview.removeAllPOIItems()
        binding.vpMapCardviewContainer.visibility = View.GONE
        stopTracking()
//        binding.flMap.removeAllViews()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class MarkerEventListener(val fragment: MapFragment, val binding: FragmentMapBinding, val karaokeList: ArrayList<Karaoke>): MapView.POIItemEventListener {


        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            val bottomNavigationView = fragment.requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView.visibility = View.GONE

            binding.kakaoMapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            val temp = p1!!.itemName.split("/")

            val karaokeObjectId = temp[0]

            // visibility setting
            binding.vpMapCardviewContainer.visibility = View.VISIBLE

            // make list here
            val foundKaraoke = karaokeList.find { it.karaokeObjectId == karaokeObjectId }


            val karaokeOrBoardList = ArrayList<KaraokeOrBoard>()
            karaokeOrBoardList.add(KaraokeOrBoard(foundKaraoke, null))

            val call = APIObject.getBoardService.getBoardList(karaokeObjectId)
            call.enqueue(object: retrofit2.Callback<GetBoardListResponseBody> {
                override fun onResponse(
                    call: Call<GetBoardListResponseBody>,
                    response: Response<GetBoardListResponseBody>
                ) {
                    if(response.isSuccessful) {
                        val data: GetBoardListResponseBody? = response.body()
                        data?.boardList?.forEach {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                            val currentDate = Calendar.getInstance().time

                            val deadLineDate = dateFormat.parse(it.deadLine)

                            // if current date is after the deadline
                            // set text color as gray
                            if(!currentDate.after(deadLineDate))
                                karaokeOrBoardList.add(KaraokeOrBoard(null, it))
                        }

                        // adapter here
                        binding.vpMapCardviewContainer.adapter = CardListAdapter(karaokeOrBoardList)
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

                    }
                }

                override fun onFailure(call: Call<GetBoardListResponseBody>, t: Throwable) {
                    Log.d("Board list GET failed", "GET failed")
                }
            })

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
            val map = fragment.requireActivity().findViewById<MapView>(R.id.kakao_mapview)
            map.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        }

        override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        }

        override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        }

    }

    private fun callKaraokeListAPI(latitude: String, longitude: String) {
        // whenever this method is called, karaoke list will grow infinitely
        // clear karaoke list
        karaokeList.clear()

        // radius is 1 km
        val call = APIObject.getKaraokeService.getKaraokeList(1000, longitude, latitude)
        call.enqueue(object: retrofit2.Callback<GetKaraokeListResponseBody> {
            override fun onResponse(
                call: Call<GetKaraokeListResponseBody>,
                response: Response<GetKaraokeListResponseBody>
            ) {
                if(response.isSuccessful) {
                    println("\n\n\nSuccessful\n\n\n")
                    val data: GetKaraokeListResponseBody? = response.body()
                    println("\n\n\n${data?.karaokeListData?.count}\n\n\n")
                    data?.karaokeListData?.karaokeList?.forEach {
                        println("\n\n\nin api call: ${it.name}\n\n\n")
                        karaokeList.add(it)
                        val marker = MapPOIItem()
                        val itemName = it.karaokeObjectId + "/" + it.name + "/" + it.address + "/" + it.roadAddress + "/" + it.phone

                        println("\n\n\n${itemName}\n\n\n")

                        marker.itemName = itemName
                        marker.mapPoint = MapPoint.mapPointWithGeoCoord(it.latitude.toDouble(), it.longitude.toDouble())
                        marker.isShowCalloutBalloonOnTouch = false
                        marker.markerType = MapPOIItem.MarkerType.BluePin
                        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                        binding.kakaoMapview.addPOIItem(marker)
                    }
                }
            }

            override fun onFailure(call: Call<GetKaraokeListResponseBody>, t: Throwable) {
                Toast.makeText(context, "노래방 검색 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

}