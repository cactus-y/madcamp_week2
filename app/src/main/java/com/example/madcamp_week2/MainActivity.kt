package com.example.madcamp_week2

import com.example.madcamp_week2.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.PutDeviceTokenRequestBody
import com.example.madcamp_week2.api.data.SuccessResponseBody
import com.example.madcamp_week2.databinding.ActivityMainBinding
import com.example.madcamp_week2.ui.chat.ChatFragment
import com.example.madcamp_week2.ui.map.MapFragment
import com.example.madcamp_week2.ui.mypage.MyPageFragment
import com.example.madcamp_week2.util.MyFirebaseMessagingService
import com.example.madcamp_week2.util.getUserToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private val fragmentManager= supportFragmentManager

    private var mapFragment: MapFragment? = null
    private var chatFragment: ChatFragment? = null
    private var mypageFragment: MyPageFragment? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_map, R.id.navigation_chat, R.id.navigation_mypage
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val openChat = intent.getBooleanExtra("openChat", false)
        Log.d("openChat", "$openChat")
        if (openChat) {
            val mapFragment = MapFragment()
            val chatFragment = ChatFragment()
            fragmentManager.beginTransaction().hide(mapFragment!!).commit()
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, chatFragment)
                .addToBackStack(null)
                .commit()
            binding.navView.selectedItemId = R.id.navigation_chat
        }



        val notiService = MyFirebaseMessagingService()
        getDeviceToken()
//        initBottomNavigation()
    }

    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener<String?> { task ->
                if (!task.isSuccessful) {
                    Log.w("Firebase Test", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                Log.d("firebase test", token)
                sendRegistrationToServer(token)
            })
    }

    private fun sendRegistrationToServer(deviceToken: String){
        val userToken = getUserToken(applicationContext)
        val body = PutDeviceTokenRequestBody(deviceToken = deviceToken)
        val call = APIObject.getDeviceTokenService.putDeviceToken("Bearer $userToken", body)
        call.enqueue(object: retrofit2.Callback<SuccessResponseBody> {
            override fun onResponse(
                call: Call<SuccessResponseBody>,
                response: Response<SuccessResponseBody>
            ) {
                if (response.isSuccessful) {

                }
                else {

                }
            }

            override fun onFailure(call: Call<SuccessResponseBody>, t: Throwable) {

            }

        })

    }

    private fun initBottomNavigation() {
        mapFragment = MapFragment()
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, mapFragment!!).commit()

        binding.navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_map -> {
                    if(mapFragment == null) {
                        mapFragment = MapFragment()
                        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, mapFragment!!).commit()
                    }
                    if(mapFragment != null) fragmentManager.beginTransaction().show(mapFragment!!).commit()
                    if(chatFragment != null) fragmentManager.beginTransaction().hide(chatFragment!!).commit()
                    if(mypageFragment != null) fragmentManager.beginTransaction().hide(mypageFragment!!).commit()

                    return@setOnItemSelectedListener true
                }
                R.id.navigation_chat -> {
                    if(chatFragment == null) {
                        chatFragment = ChatFragment()
                        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, chatFragment!!).commit()
                    }
                    if(mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!).commit()
                    if(chatFragment != null) fragmentManager.beginTransaction().show(chatFragment!!).commit()
                    if(mypageFragment != null) fragmentManager.beginTransaction().hide(mypageFragment!!).commit()

                    return@setOnItemSelectedListener true
                }
                R.id.navigation_mypage -> {
                    if(mypageFragment == null) {
                        mypageFragment = MyPageFragment()
                        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, mypageFragment!!).commit()
                    }
                    if(mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!).commit()
                    if(chatFragment != null) fragmentManager.beginTransaction().hide(chatFragment!!).commit()
                    if(mypageFragment != null) fragmentManager.beginTransaction().show(mypageFragment!!).commit()

                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
    }
}