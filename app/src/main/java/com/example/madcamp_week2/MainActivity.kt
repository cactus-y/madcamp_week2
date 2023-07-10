package com.example.madcamp_week2

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
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

//        binding.navView.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.navigation_map -> {
//                    binding.tvToolbarTitle.text = "Map"
//                    return@setOnItemSelectedListener true
//                }
//                R.id.navigation_chat -> {
//                    binding.tvToolbarTitle.text = "Chat"
//                    return@setOnItemSelectedListener true
//                }
//                R.id.navigation_mypage -> {
//                    binding.tvToolbarTitle.text = "Reservation"
//                    return@setOnItemSelectedListener true
//                }
//                else -> {
//                    return@setOnItemSelectedListener false
//                }
//            }
//        }

//        initBottomNavigation()
    }

//    override fun onCreateContextMenu(
//        menu: ContextMenu?,
//        v: View?,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        if(v == findViewById(R.id.nav_host_fragment_activity_main)) {
//            menuInflater.inflate(R.menu.map_check_request, menu)
//        }
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.action_menu_map_check_request -> {
//                showPopupWindow()
//            }
//        }
//        return super.onContextItemSelected(item)
//    }
//
//    private fun showPopupWindow() {
//        val inflater = LayoutInflater.from(this)
//        val popupView = inflater.inflate(R.layout.map_notification_popup_window, null)
//
//        val rcv_map_request_list = popupView.findViewById<RecyclerView>(R.id.rcv_map_request_list)
//        val tv_map_request_empty_list = popupView.findViewById<TextView>(R.id.tv_map_request_empty_list)
//
//        // when the list is empty, rcv becomes invisible
//        if(true) {
//            rcv_map_request_list.visibility = View.GONE
//            tv_map_request_empty_list.visibility = View.VISIBLE
//        }
//
//        val popupWindow = PopupWindow(
//            popupView,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            true
//        )
//
//        popupWindow.showAsDropDown()
//    }



//    private fun initBottomNavigation() {
//        mapFragment = MapFragment()
//        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, mapFragment!!).commit()
//
//        binding.navView.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.navigation_map -> {
//                    if(mapFragment == null) {
//                        mapFragment = MapFragment()
//                        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, mapFragment!!).commit()
//                    }
//                    if(mapFragment != null) fragmentManager.beginTransaction().show(mapFragment!!).commit()
//                    if(chatFragment != null) fragmentManager.beginTransaction().hide(chatFragment!!).commit()
//                    if(mypageFragment != null) fragmentManager.beginTransaction().hide(mypageFragment!!).commit()
//
//                    return@setOnItemSelectedListener true
//                }
//                R.id.navigation_chat -> {
//                    if(chatFragment == null) {
//                        chatFragment = ChatFragment()
//                        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, chatFragment!!).commit()
//                    }
//                    if(mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!).commit()
//                    if(chatFragment != null) fragmentManager.beginTransaction().show(chatFragment!!).commit()
//                    if(mypageFragment != null) fragmentManager.beginTransaction().hide(mypageFragment!!).commit()
//
//                    return@setOnItemSelectedListener true
//                }
//                R.id.navigation_mypage -> {
//                    if(mypageFragment == null) {
//                        mypageFragment = MyPageFragment()
//                        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_activity_main, mypageFragment!!).commit()
//                    }
//                    if(mapFragment != null) fragmentManager.beginTransaction().hide(mapFragment!!).commit()
//                    if(chatFragment != null) fragmentManager.beginTransaction().hide(chatFragment!!).commit()
//                    if(mypageFragment != null) fragmentManager.beginTransaction().show(mypageFragment!!).commit()
//
//                    return@setOnItemSelectedListener true
//                }
//                else -> {
//                    return@setOnItemSelectedListener true
//                }
//            }
//        }
//    }
}