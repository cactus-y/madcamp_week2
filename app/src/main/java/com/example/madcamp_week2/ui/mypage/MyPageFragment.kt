package com.example.madcamp_week2.ui.mypage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.board.GetMyBoardListResponseBody
import com.example.madcamp_week2.api.data.board.MyBoard
import com.example.madcamp_week2.api.data.guest.GetMyGusetListResponseBody
import com.example.madcamp_week2.api.data.guest.MyGuest
import com.example.madcamp_week2.databinding.FragmentMypageBinding
import com.example.madcamp_week2.getUserInfoFromToken
import com.example.madcamp_week2.getUserToken
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response


class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val user = getUserInfoFromToken(requireContext())
        val userToken = "Bearer ${getUserToken(requireContext())}"
        Log.d("JWT user", "$user")
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        binding.tvMypageUserEditButton.setOnClickListener {
//            Intent(
//                context,
//                UserEditActivity::class.java
//            ).run { requireContext().startActivity(this) }
//        }

        binding.tvMypageMyNickname.text = user.nickname
        binding.tvMypageMyEmail.text = user.email
        if (user.profileImage != null) {
            Picasso.get().load(user.profileImage).memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(com.example.madcamp_week2.R.drawable.placeholder_image)
                .into(binding.ivMypageUserEditProfileImage)
        }

        // genre dynamic textview here
        for(i: Int in 0 until user.musicGenre.size) {
            val textView = TextView(context)
            textView.text = user.musicGenre[i]
            textView.setTextColor(Color.parseColor("#E45477"))
            textView.textSize = 10f
            textView.setPadding(8, 8, 8, 8)
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.background_circular_dynamic)
            textView.id = i

            val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            param.marginStart = 8
            param.marginEnd = 8
            param.topMargin = 4
            param.bottomMargin = 4

            textView.layoutParams = param

            if(i > 5) {
                binding.llMypageMyGenreContainer2.visibility = View.VISIBLE
                binding.llMypageMyGenreContainer2.addView(textView)
            } else {
                binding.llMypageMyGenreContainer1.addView(textView)
            }
        }

        val tempView1 = View(context)
        val tempParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        tempParam.weight = 1f
        tempView1.layoutParams = tempParam
        binding.llMypageMyGenreContainer1.addView(tempView1)
        if(binding.llMypageMyGenreContainer2.visibility == View.VISIBLE) {
            val tempView2 = View(context)
            val tempParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            tempParam.weight = 1f
            tempView2.layoutParams = tempParam
            binding.llMypageMyGenreContainer2.addView(tempView2)
        }

        // my board list recycler view
        val callBoardList = APIObject.getMyPageService.getMyBoardList(userToken)
        callBoardList.enqueue(object: retrofit2.Callback<GetMyBoardListResponseBody> {
            override fun onResponse(
                call: Call<GetMyBoardListResponseBody>,
                response: Response<GetMyBoardListResponseBody>
            ) {
                if(response.isSuccessful) {
                    val list = ArrayList<MyBoard>()
                    val data: GetMyBoardListResponseBody? = response.body()

                    data?.boardList?.forEach {
                        list.add(it)
                    }

                    // sort the list
                    list.sortByDescending { it.deadLine }

                    binding.rcvMypageMyBoardList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.rcvMypageMyBoardList.adapter = MyBoardListAdapter(list)
                } else {
                    Toast.makeText(context, "에러!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetMyBoardListResponseBody>, t: Throwable) {
                Toast.makeText(context, "에러!", Toast.LENGTH_SHORT).show()
            }
        })



        // guest board list recycler view
        val callGuestList = APIObject.getMyPageService.getMyGuestList(userToken)
        callGuestList.enqueue(object: retrofit2.Callback<GetMyGusetListResponseBody> {
            override fun onResponse(
                call: Call<GetMyGusetListResponseBody>,
                response: Response<GetMyGusetListResponseBody>
            ) {
                if(response.isSuccessful) {
                    val list = ArrayList<MyGuest>()
                    val data: GetMyGusetListResponseBody? = response.body()

                    data?.boardList?.forEach {
                        list.add(it)
                    }

                    // sort the list
                    list.sortByDescending { it.deadLine }

                    binding.rcvMypageGuestBoardList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.rcvMypageGuestBoardList.adapter = GuestBoardListAdapter(list)
                } else {
                    Toast.makeText(context, "에러!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetMyGusetListResponseBody>, t: Throwable) {
                Toast.makeText(context, "에러!", Toast.LENGTH_SHORT).show()
            }
        })


        return root
    }

    // refresh
//    override fun onResume() {
//        super.onResume()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}