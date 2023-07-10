package com.example.madcamp_week2

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.madcamp_week2.databinding.ActivityMainBinding
import com.example.madcamp_week2.ui.chat.ChatFragment
import com.example.madcamp_week2.ui.map.MapFragment
import com.example.madcamp_week2.ui.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    private val fragmentManager= supportFragmentManager

    private var mapFragment: MapFragment? = null
    private var chatFragment: ChatFragment? = null
    private var mypageFragment: MyPageFragment? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.lifecycleOwner = this

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

//        initBottomNavigation()
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