package com.example.madcamp_week2.ui.mypage

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.madcamp_week2.MainActivity
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.PostUserResponseBody
import com.example.madcamp_week2.databinding.ActivityUserEditBinding
import com.example.madcamp_week2.sample.genres
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class UserEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserEditBinding
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val isChecked = BooleanArray(genres.size)
    private val checkBoxList: ArrayList<CheckBox> = ArrayList()
    private var gender: Boolean? = null
    private val permission: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_EXTERNAL_STORAGE else android.Manifest.permission.READ_MEDIA_IMAGES
    private var permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(), ActivityResultCallback<Boolean> { result ->
        if (result) {
        }
        else {

        }
    })
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(com.example.madcamp_week2.R.drawable.placeholder_image).into(binding.ivMypageUserEditProfileImage)
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.madcamp_week2.R.layout.activity_user_edit)
        pref = applicationContext.getSharedPreferences(getString(R.string.pref_key), Activity.MODE_PRIVATE);
        editor = pref.edit()
        binding = ActivityUserEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("nickname")
        val userEmail = intent.getStringExtra("email")
        val genreSize = intent.getIntExtra("genreSize", -1)

        if (genreSize != -1) {
            for(i: Int in 0 until genreSize) {
                isChecked[intent.getIntExtra(i.toString(), -1)] = true
            }
        }

        binding.etMypageUserEditNickname.setText(userName)
        binding.emailTextView.text = userEmail
        // Picasso.get().load(profileImage).memoryPolicy(MemoryPolicy.NO_CACHE ).placeholder(com.example.madcamp_week2.R.drawable.placeholder_image).error(com.example.madcamp_week2.R.drawable.ic_user_default_profile_image).into(binding.ivMypageUserEditProfileImage)
        checkPermission();
        binding.ivMypageUserEditProfileImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.radioGroupGender.setOnCheckedChangeListener { _, i ->
            when(i) {
                com.example.madcamp_week2.R.id.radioButtonMale -> gender = true
                com.example.madcamp_week2.R.id.radioButtonFemale -> gender = false
            }
        }

        for(i:Int in isChecked.indices) {
            val checkBox = CheckBox(this)
            checkBox.text = genres[i]
            checkBox.textSize = 16f
            checkBox.isChecked = isChecked[i]
            checkBox.id = i
            checkBoxList.add(checkBox)

            val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            checkBox.layoutParams = param
            binding.llMypageUserMusicGenreContainer.addView(checkBox)
        }


        binding.saveButton.setOnClickListener {
            if (binding.etMypageUserEditNickname.text.toString() == "") {
                Toast.makeText(applicationContext, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else if (!binding.radioButtonFemale.isChecked && !binding.radioButtonMale.isChecked) {
                Toast.makeText(applicationContext, "성별을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val musicGenre = arrayListOf<String>()
                for(i:Int in 0 until checkBoxList.size) {
                    if (checkBoxList[i].isChecked) {
                        val genre = genres[i]
                        musicGenre.add("\"$genre\"")
                    }
                }
                if (musicGenre.size == 0) {
                    Toast.makeText(applicationContext, "노래 장르를 최소 1개 이상 선택해주세요", Toast.LENGTH_SHORT).show()
                }
                else {
                    val nickname = binding.etMypageUserEditNickname.text.toString()
                    val jsonObject = JSONObject("{\"email\":\"${userEmail}\",\"nickname\":\"${nickname}\",\"gender\": ${gender},\"musicGenre\":${musicGenre}}").toString()
                    // val filePart = MultipartBody.Part.createFormData("file", file.name, RequestBody.create(MediaType.parse("multipart/form-data"), file))
                    val dataPart = MultipartBody.Part.createFormData("data", jsonObject)
                    Log.d("data part", "$jsonObject")
                    val call = APIObject.getUserService.createUser(data = dataPart, file = null)
                    call.enqueue(object: retrofit2.Callback<PostUserResponseBody> {
                        override fun onResponse(
                            call: Call<PostUserResponseBody>,
                            response: Response<PostUserResponseBody>
                        ) {
                            val data: PostUserResponseBody? = response.body()
                            if (response.isSuccessful) {
                                Log.d("success", "$data")
                                editor.putString(getString(R.string.token_key), data!!.token)
                                editor.putString(getString(R.string.user_nickname_key), data!!.user!!.nickname)
                                editor.apply()
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                            }
                            else {
                                val message = data?.message;
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<PostUserResponseBody>, t: Throwable) {
                            val message = if (t.message != null) t.message else "An error occurred!"
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d("main activity", "permission accepted")
        }
        else {
            permissionLauncher.launch(permission)
        }
    }
}