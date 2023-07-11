package com.example.madcamp_week2.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.databinding.FragmentMypageBinding
import com.example.madcamp_week2.util.getUserInfoFromToken
import com.example.madcamp_week2.sample.karaoke1
import com.example.madcamp_week2.sample.karaoke2
import com.example.madcamp_week2.sample.karaoke3
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso


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
        Log.d("JWT user", "$user")
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
//        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        val root: View = binding.root

//        binding.tvMypageUserEditButton.setOnClickListener {
//            Intent(
//                context,
//                UserEditActivity::class.java
//            ).apply {
////                putExtra("profileImageUri", "")
//                putExtra("nickname", user.nickname)
//                putExtra("email", user.email)
//                putExtra("genreSize", user.musicGenre.size)
//                putExtra("gender", if (user.gender) "female" else "male")
//                putExtra("profileImage", user.profileImage)
//
//                for(i: Int in 0 until user.musicGenre.size) {
//                    for(j: Int in 0 until genres.size)  {
//                        if (genres[j] == user.musicGenre[i]) {
//                            putExtra(i.toString(), j)
//                        }
//                    }
//                }
//
//            }.run { requireContext().startActivity(this) }
//        }

        binding.nicknameTextView.text = user.nickname
        binding.userEmailTextView.text = user.email
        if (user.profileImage != null) {
            Picasso.get().load(user.profileImage).memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(com.example.madcamp_week2.R.drawable.placeholder_image)
                .into(binding.ivMypageUserEditProfileImage)
        }

        for(i: Int in 0 until user.musicGenre.size) {
            val textView = TextView(context)
            textView.text = user.musicGenre[i]
            textView.textSize = 16f
            textView.id = i

            val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            param.topMargin = 8
            param.bottomMargin = 8

            textView.layoutParams = param
            binding.llMypageUserMusicGenreContainer.addView(textView)
        }

        binding.rcvMypageHistoryList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvMypageHistoryList.adapter = KaraokeHistoryListAdapter(arrayListOf(karaoke1, karaoke2, karaoke3, karaoke1, karaoke2, karaoke3))

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