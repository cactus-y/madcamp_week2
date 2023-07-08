package com.example.madcamp_week2.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.databinding.FragmentMypageBinding
import com.example.madcamp_week2.sample.SampleKaraoke
import com.example.madcamp_week2.sample.genres
import com.example.madcamp_week2.sample.karaoke1
import com.example.madcamp_week2.sample.karaoke2
import com.example.madcamp_week2.sample.karaoke3
import com.example.madcamp_week2.sample.user1

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
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvMypageUserEditButton.setOnClickListener {
            // linearlayout's dynamic textviews should be removed
            Intent(
                context,
                UserEditActivity::class.java
            ).apply {
//                putExtra("profileImageUri", "")
                putExtra("userName", user1.name)
                putExtra("userEmail", user1.email)
                putExtra("genreSize", user1.genreInIndex.size)
                for(i: Int in 0 until user1.genreInIndex.size) {
                    putExtra(i.toString(), user1.genreInIndex[i])
                }

            }.run { requireContext().startActivity(this) }
        }

        binding.tvMypageUserNickname.text = user1.name
        binding.tvMypageUserEmail.text = user1.email

        for(i: Int in 0 until user1.genreInIndex.size) {
            val textView = TextView(context)
            textView.text = genres[user1.genreInIndex[i]]
            textView.textSize = 12f
            textView.setPadding(8, 8, 8, 8)
            textView.id = i

            val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            param.marginStart = 8
            param.marginEnd = 8

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