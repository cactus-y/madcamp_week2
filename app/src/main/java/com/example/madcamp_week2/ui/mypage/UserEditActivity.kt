package com.example.madcamp_week2.ui.mypage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.madcamp_week2.MainActivity
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.user.PostUserResponseBody
import com.example.madcamp_week2.databinding.ActivityUserEditBinding
import com.example.madcamp_week2.sample.genres
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File


class UserEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserEditBinding
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val isChecked = BooleanArray(genres.size)
    private val checkBoxList: ArrayList<CheckBox> = ArrayList()
    private var file: File? = null
    private var gender: Boolean? = null
    private val permission: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { Manifest.permission.READ_MEDIA_IMAGES } else { Manifest.permission.READ_EXTERNAL_STORAGE }
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            imagePicker.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI))
        } else {
            Toast.makeText(this, "권한을 받아오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private val imagePicker = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                if (imgUri != null) {
                    Log.d("Image Uri", "$imgUri")
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(imgUri, projection, null, null, null)
                    if (cursor != null) {
                        val columnIndex =
                            cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        cursor.moveToNext()
                        val path = cursor.getString(columnIndex)
                        Log.d("imagePicker", "$path")
                        file = File(path)
                        Log.d("imagePicker file", "$file")
                        cursor.close()
                    }
                    Picasso.get().load(imgUri).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(com.example.madcamp_week2.R.drawable.placeholder_image)
                        .into(binding.ivMypageUserEditProfileImage)
                }
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
        val initialGender = intent.getStringExtra("gender")
        val profileImage = intent.getStringExtra("profileImage")
        if (profileImage != null) {
            Picasso.get().load(profileImage).memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(com.example.madcamp_week2.R.drawable.placeholder_image)
                .into(binding.ivMypageUserEditProfileImage)
        }
        if (initialGender == "male") {
            gender = true
            binding.radioButtonMale.isChecked = true
        }
        else if (initialGender == "female") {
            gender = false
            binding.radioButtonFemale.isChecked = false
        }
        if (genreSize != -1) {
            for(i: Int in 0 until genreSize) {
                isChecked[intent.getIntExtra(i.toString(), -1)] = true
            }
        }

        binding.nicknameEditTextView.setText(userName)
        binding.nicknameEditTextView.imeOptions = EditorInfo.IME_ACTION_DONE;
        binding.emailTextView.text = userEmail
        binding.ivMypageUserEditProfileImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@UserEditActivity, permission) == PackageManager.PERMISSION_GRANTED) {
                imagePicker.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI))
            } else {
                permissionLauncher.launch(permission)
            }
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
            if (binding.nicknameEditTextView.text.toString() == "") {
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
                    val nickname = binding.nicknameEditTextView.text.toString()
                    val jsonObject = JSONObject("{\"email\":\"${userEmail}\",\"nickname\":\"${nickname}\",\"gender\": ${gender},\"musicGenre\":${musicGenre}}").toString()
                    val filePart = if (file == null) { null } else { MultipartBody.Part.createFormData("file", file!!.name, RequestBody.create(
                        MediaType.parse("image/*"), file)) }
                    val dataPart = MultipartBody.Part.createFormData("data", jsonObject)
                    Log.d("data part", "$jsonObject")
                    Log.d("file part", "$filePart")
                    val call = APIObject.getUserService.createUser(data = dataPart, file = filePart)
                    call.enqueue(object: retrofit2.Callback<PostUserResponseBody> {
                        override fun onResponse(
                            call: Call<PostUserResponseBody>,
                            response: Response<PostUserResponseBody>
                        ) {
                            val data: PostUserResponseBody? = response.body()
                            if (response.isSuccessful) {
                                Log.d("success", "$data")
                                editor.putString(getString(R.string.token_key), data!!.token)
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
}