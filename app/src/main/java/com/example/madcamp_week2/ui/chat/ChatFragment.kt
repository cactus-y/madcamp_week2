package com.example.madcamp_week2.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.R
import com.example.madcamp_week2.databinding.FragmentChatBinding
import com.example.madcamp_week2.sample.chatRoom1
import com.example.madcamp_week2.sample.chatRoom2
import com.example.madcamp_week2.sample.globalChatRoomList

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentChatBinding.inflate(inflater, container, false)
//        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        val root: View = binding.root

        binding.rcvChatList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val decoCount = binding.rcvChatList.itemDecorationCount
        if(decoCount != 0) {
            binding.rcvChatList.removeItemDecorationAt(decoCount - 1)
        }
        binding.rcvChatList.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

        binding.rcvChatList.adapter = ChatListAdapter(globalChatRoomList)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}