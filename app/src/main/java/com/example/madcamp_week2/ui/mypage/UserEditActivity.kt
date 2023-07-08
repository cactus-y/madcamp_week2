package com.example.madcamp_week2.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.ActivityUserEditBinding
import com.example.madcamp_week2.sample.genres

class UserEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserEditBinding
    val isChecked = BooleanArray(9)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        binding = ActivityUserEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val userEmail = intent.getStringExtra("userEmail")
        val genreSize = intent.getIntExtra("genreSize", -1)

        if(genreSize != -1) {
            for(i: Int in 0 until genreSize) {
                isChecked[intent.getIntExtra(i.toString(), -1)] = true
            }
        }

        binding.etMypageUserEditNickname.setText(userName)
        binding.etMypageUserEmail.setText(userEmail)

        for(i:Int in 0 until 9) {
            val checkBox = CheckBox(this)
            checkBox.setText(genres[i])
            checkBox.textSize = 16f
            checkBox.isChecked = isChecked[i]
            checkBox.id = i

            val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            param.marginStart = 8
//            param.marginEnd = 8

            checkBox.layoutParams = param
            binding.llMypageUserMusicGenreContainer.addView(checkBox)
        }


//        binding.tvMypageUserSaveButton.setOnClickListener {  }


    }
}