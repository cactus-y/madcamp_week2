package com.example.madcamp_week2.ui.auth

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp_week2.MainActivity
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.SocialLoginRequestBody
import com.example.madcamp_week2.api.data.SocialLoginResponseBody
import com.example.madcamp_week2.databinding.ActivityLoginBinding
import com.example.madcamp_week2.ui.mypage.UserEditActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Response

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == -1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }
    private lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        pref = applicationContext.getSharedPreferences(getString(R.string.pref_key), Activity.MODE_PRIVATE)
        setContentView(binding.root)
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.server_client_id)).requestEmail().build()
        val gsc: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.loginButton.setOnClickListener {
            signIn(gsc)
        }
    }

    private fun signIn(gsc: GoogleSignInClient) {
        val signInIntent = gsc.signInIntent
        googleLoginLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            callGoogleSocialLoginAPI(idToken = account.idToken!!, email = account.email!!)
        }
        catch (e: ApiException) {
            Log.w("Main Activity", "signInResult:failed code=" + e.statusCode)
        }
    }
    private fun callGoogleSocialLoginAPI(idToken: String, email: String) {
        val requestBody = SocialLoginRequestBody(
            idToken,
            email
        )
        Log.d("request", email)
        Log.d("request", idToken)
        val call = APIObject.getSocialLoginService.loginWithGoogle(requestBody)
        call.enqueue(object: retrofit2.Callback<SocialLoginResponseBody> {
            override fun onResponse(
                call: Call<SocialLoginResponseBody>,
                response: Response<SocialLoginResponseBody>
            ) {
                if (response.isSuccessful) {
                    val data: SocialLoginResponseBody? = response.body()
                    if (data?.token == null) {
                        Log.d("login activity", "$data")
                        val userEditActivity = Intent(applicationContext, UserEditActivity::class.java)
                        userEditActivity.putExtra("email", data?.user?.email)
                        userEditActivity.putExtra("nickname", data?.user?.nickname)
                        userEditActivity.putExtra("profileImage", data?.user?.profileImage)
                        startActivity(userEditActivity)
                        finish()
                    }
                    else {
                        val editor = pref.edit()
                        editor.putString(getString(R.string.token_key), data!!.token)
                        editor.apply()
                        val mainActivity = Intent(applicationContext, MainActivity::class.java)
                        startActivity(mainActivity)
                        finish()
                    }
                }
            }
            override fun onFailure(call: Call<SocialLoginResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        })

    }
}